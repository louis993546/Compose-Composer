package com.louis993546.composecomposer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.ui.theme.ComposeComposerTheme

class MainActivity : ComponentActivity() {
    companion object {
        // TODO this should be store in settings
        private val panelOrders = listOf(
            Panel.Tree, Panel.Renderer, Panel.Properties
        )
    }

    private val page = Page(
        width = 360.dp,
        height = 640.dp,
        backgroundColor = Color.Gray,
        node = Node.Column(
            children = listOf(
                Node.Text(text = "text 1"),
                Node.Text(text = "text 2"),
                Node.Text(text = "text 3"),
                Node.Text(text = "text 4"),
                Node.Row(
                    children = listOf(
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 2"),
                        Node.Text(text = "text 3"),
                    )
                ),
                Node.Checkbox(text = "Checkbox", checked = false),
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeComposerTheme {
                Scaffold(
                    topBar = { TopBar() }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colors.background
                    ) {
                        Body(panels = panelOrders, page = page)
                    }
                }
            }
        }
    }
}

@Composable
fun Body(
    modifier: Modifier = Modifier,
    panels: List<Panel>,
    page: Page,
) {
    Row(modifier = modifier) {
        panels.forEachIndexed { index, panel ->
            when (panel) {
                Panel.Tree -> Page(
                    modifier = Modifier.weight(1f),
                    page = page
                )
                Panel.Renderer -> PageRenderer(
                    modifier = Modifier.weight(1f),
                    page = page
                )
                Panel.Properties -> Properties(modifier = Modifier.weight(1f))
            }
            if (index < panels.size) PanelDivider()
        }
    }
}

@Composable
fun PageRenderer(
    modifier: Modifier,
    page: Page,
) {
    NodeRenderer(
        modifier = modifier
            .background(color = page.backgroundColor)
            .size(width = page.width, height = page.height), // TODO make it sit in the middle of the screen
        node = page.node
    )
}

@Composable
fun Page(
    modifier: Modifier = Modifier,
    page: Page,
) {
    Tree(
        modifier = modifier,
        node = page.node,
    )
}

/**
 * Copy of [_root_ide_package_.androidx.compose.material.Divider()] but vertical
 */
@Composable
fun PanelDivider(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
    )
}

@Composable
fun TopBar() {
    TopAppBar {
        Text("Composer")
    }
}

enum class Panel {
    Tree,
    Renderer,
    Properties
}

@Composable
fun NodeRenderer(
    modifier: Modifier = Modifier,
    node: Node,
    rowScope: RowScope? = null,
    columnScope: ColumnScope? = null
) {
    Box(modifier = modifier) {
        when (node) {
            is Node.Checkbox -> Checkbox(checked = node.checked, onCheckedChange = {
                TODO("need to modify this node, probably by propagate it back up")
            })
            is Node.Column -> Column {
                node.children.forEach { child ->
                    NodeRenderer(node = child, columnScope = this)
                }
            }
            is Node.Image -> Text(text = "TODO image") // TODO
            is Node.RadioGroup -> Text("TODO radio group")
            is Node.Row -> Row {
                node.children.forEach { child ->
                    NodeRenderer(node = child, rowScope = this)
                }
            }
            is Node.Text -> Text(text = node.text)
        }
    }
}

/**
 * TODO
 *   - icons for each type of node instead of name in text
 */
@Composable
fun Tree(
    modifier: Modifier = Modifier,
    node: Node,
) {
    Box(modifier = modifier) {
        when (node) {
            is Node.Checkbox -> {
                NodeDescription(
                    icon = R.drawable.ic_baseline_check_box_outline_blank_24,
                    contentDescription = "Check box",
                    text = "${node.text} = ${node.checked}"
                )
            }
            is Node.Column -> {
                Column {
                    NodeDescription(
                        icon = R.drawable.ic_baseline_view_column_24,
                        contentDescription = "Row",
                        text = "(${node.children.size})"
                    )
                    node.children.forEach { child ->
                        Tree(
                            modifier = Modifier.padding(start = 16.dp),
                            node = child,
                        )
                    }
                }
            }
            is Node.Image -> {
                NodeDescription(
                    icon = R.drawable.ic_baseline_image_24,
                    contentDescription = "Image",
                    text = "(TODO some description)"
                )
            }
            is Node.RadioGroup -> {
                NodeDescription(
                    icon = R.drawable.ic_baseline_radio_button_checked_24,
                    contentDescription = "Radio Group",
                    text = "(TODO some description)"
                )
            }
            is Node.Row -> {
                Column {
                    NodeDescription(
                        icon = R.drawable.ic_baseline_table_rows_24,
                        contentDescription = "Row",
                        text = "${node.children.size} items"
                    )
                    node.children.forEach { child ->
                        Tree(
                            modifier = Modifier.padding(start = 16.dp),
                            node = child,
                        )
                    }
                }
            }
            is Node.Text -> {
                NodeDescription(
                    icon = R.drawable.ic_baseline_text_fields_24,
                    contentDescription = "Text",
                    text = "${node.text}"
                )
            }
        }
    }
}

@Composable
fun NodeDescription(
    @DrawableRes icon: Int,
    contentDescription: String,
    text: String,
) {
    Row {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text)
    }
}

@Composable
fun Properties(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Text("Properties")
    }
}

sealed class Node {
    data class Text(val text: String) : Node()
    data class Image(val url: String) : Node()
    data class Row(val children: List<Node>) : Node()
    data class Column(val children: List<Node>) : Node()
    data class Checkbox(val text: String, val checked: Boolean) : Node()
    data class RadioGroup(val options: List<String>, val selection: Int?) : Node()
}

data class Page(
    val width: Dp,
    val height: Dp,
    val backgroundColor: Color,
    val node: Node,
)