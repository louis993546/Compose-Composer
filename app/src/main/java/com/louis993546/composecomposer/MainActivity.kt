package com.louis993546.composecomposer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.ui.properties.Properties
import com.louis993546.composecomposer.ui.renderer.PageRenderer
import com.louis993546.composecomposer.ui.tree.Tree
import com.louis993546.composecomposer.ui.theme.ComposeComposerTheme
import com.louis993546.composecomposer.util.exhaustive
import com.louis993546.composecomposer.util.randId

class MainActivity : ComponentActivity() {
    companion object {
        // TODO this should be store in settings
        private val panelOrders = listOf(
            Panel.Tree, Panel.Renderer, Panel.Properties
        )
    }

    private val defaultPage = Page(
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
                    ),
                ),
                Node.Checkbox(text = "Checkbox", checked = false),
            ),
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var page by remember { mutableStateOf(defaultPage) }

            ComposeComposerTheme {
                Scaffold(
                    topBar = { TopBar() }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colors.background
                    ) {
                        Body(
                            panels = panelOrders,
                            page = page,
                            updateNode = { newNode ->
                                page = page.copyWithNewNode(newNode)
                            },
                        )
                    }
                }
            }
        }
    }

    private fun Page.copyWithNewNode(newNode: Node): Page = this.copy(
        node = this.node.copyWithNewNode(newNode)
    )

    private fun Node.copyWithNewNode(newNode: Node): Node = when {
        this.id == newNode.id -> newNode
        this is Node.Column -> this.copy(
            children = this.children.map { it.copyWithNewNode(newNode) }
        )
        this is Node.Row -> this.copy(
            children = this.children.map { it.copyWithNewNode(newNode) }
        )
        else -> this
    }
}

@Composable
fun Body(
    modifier: Modifier = Modifier,
    panels: List<Panel>,
    page: Page,
    updateNode: (Node) -> Unit,
) {
    var currentlySelectedNode by remember { mutableStateOf<Node?>(null) }

    Row(modifier = modifier) {
        panels.forEachIndexed { index, panel ->
            when (panel) {
                Panel.Tree -> Page(
                    modifier = Modifier.weight(1f),
                    page = page,
                    onNodeSelected = { node -> currentlySelectedNode = node }
                )
                Panel.Renderer -> PageRenderer(
                    modifier = Modifier.weight(1f),
                    page = page,
                )
                Panel.Properties -> Properties(
                    modifier = Modifier.weight(1f),
                    node = currentlySelectedNode,
                    onNodeModified = { newNode -> updateNode(newNode) }
                )
            }.exhaustive
            if (index < panels.size) PanelDivider()
        }
    }
}

@Composable
fun Page(
    modifier: Modifier = Modifier,
    page: Page,
    onNodeSelected: (Node) -> Unit,
) {
    Tree(
        modifier = modifier,
        node = page.node,
        onNodeSelected = onNodeSelected,
    )
}

/**
 * Copy of [androidx.compose.material.Divider] but vertical
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

typealias Id = Int

sealed class Node {
    abstract val id: Id

    data class Text(
        override val id: Id = randId(),
        val text: String,
    ) : Node()

    data class Image(
        override val id: Id = randId(),
        val url: String,
    ) : Node()

    data class Row(
        override val id: Id = randId(),
        val children: List<Node>,
    ) : Node()

    data class Column(
        override val id: Id = randId(),
        val children: List<Node>,
    ) : Node()

    data class Checkbox(
        override val id: Id = randId(),
        val text: String,
        val checked: Boolean,
    ) : Node()

    data class RadioGroup(
        override val id: Id = randId(),
        val options: List<String>,
        val selection: Int?,
    ) : Node()
}

/**
 * TODO border color & thickness & radius
 *   nice to have: warning/hint when border color & [backgroundColor] are too similar
 */
data class Page(
    val width: Dp,
    val height: Dp,
    val backgroundColor: Color,
    val node: Node,
)
