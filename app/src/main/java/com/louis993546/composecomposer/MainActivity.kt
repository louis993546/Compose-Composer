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
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dev.zacsweers.moshix.sealed.annotations.DefaultNull
import dev.zacsweers.moshix.sealed.annotations.TypeLabel
import timber.log.Timber

class MainActivity : ComponentActivity() {
    companion object {
        // TODO this should be store in settings
        private val panelOrders = listOf(
            Panel.Tree, Panel.Renderer, Panel.Properties
        )
    }

    private val moshi = Moshi.Builder()
//        .add(DpMoshiAdapter())
//        .add(ColorMoshiAdapter())
        .build()

    private val defaultPage = Page(
        widthFloat = 360.dp.value,
        heightFloat = 640.dp.value,
        backgroundColorLong = Color.Gray.value.toLong(),
        node = Node.Column(
            children = listOf(
                Node.Text(text = "text 1"),
                Node.Text(text = "text 2"),
                Node.Image(
                    url = "https://pbs.twimg.com/profile_images/1377946002473254912/h3q66L7m_400x400.png",
                    contentDescription = "",
                ),
                Node.Text(text = "text 3"),
                Node.Text(text = "text 4"),
                Node.Row(
                    children = listOf(
                        Node.Text(text = "text 1"),
                        Node.Column(
                            children = listOf(
                                Node.Text(text = "text 1"),
                                Node.Row(
                                    children = listOf(
                                        Node.Text(text = "text 1"),
                                        Node.Column(
                                            children = listOf(
                                                Node.Text(text = "text 1"),
                                                Node.Row(
                                                    children = listOf(
                                                        Node.Text(text = "text 1"),
                                                    )
                                                ),
                                            )
                                        ),
                                    )
                                ),
                            )
                        ),
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
                    topBar = { TopBar(testOnClick = {
                        Timber.d(moshi.adapter(Page::class.java).toJson(page))
                    }) }
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
                                // TODO if there is any validation,
                                //  then maybe copyWithNewNode should return
                                //  the new new node as well
                                newNode
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

    /**
     * TODO skip reset of the tree once a single replacement has been done
     */
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
    updateNode: (Node) -> Node,
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
                    onNodeModified = { newNode ->
                        currentlySelectedNode = updateNode(newNode)
                    }
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
fun TopBar(
    modifier: Modifier = Modifier,
    testOnClick: () -> Unit,
) {
    TopAppBar(modifier = modifier) {
        Text("Composer")
        Button(onClick = testOnClick) {
            Text("Serialize to logcat")
        }
    }
}

enum class Panel {
    Tree,
    Renderer,
    Properties
}

typealias Id = Int

@DefaultNull
@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed class Node {
    abstract val id: Id

    @TypeLabel("text")
    @JsonClass(generateAdapter = true)
    data class Text(
        override val id: Id = randId(),
        val text: String,
    ) : Node()

    @TypeLabel("image")
    @JsonClass(generateAdapter = true)
    data class Image(
        override val id: Id = randId(),
        val url: String,
        val contentDescription: String,
    ) : Node()

    @TypeLabel("row")
    @JsonClass(generateAdapter = true)
    data class Row(
        override val id: Id = randId(),
        val children: List<Node>,
    ) : Node()

    @TypeLabel("column")
    @JsonClass(generateAdapter = true)
    data class Column(
        override val id: Id = randId(),
        val children: List<Node>,
    ) : Node()

    @TypeLabel("checkbox")
    @JsonClass(generateAdapter = true)
    data class Checkbox(
        override val id: Id = randId(),
        val text: String,
        val checked: Boolean,
    ) : Node()

    @TypeLabel("radio_group")
    @JsonClass(generateAdapter = true)
    data class RadioGroup(
        override val id: Id = randId(),
        val options: List<String>,
        val selection: Int?,
    ) : Node()
}

/**
 * TODO border color & thickness & radius
 *   nice to have: warning/hint when border color & [backgroundColor] are too similar
 *
 * TODO How to get codegen to see Dp/Color with it's custom moshi adapter
 */
@JsonClass(generateAdapter = true)
data class Page(
//    val width: Dp,
//    val height: Dp,
    val widthFloat: Float,
    val heightFloat: Float,
    val backgroundColorLong: Long,
    val node: Node,
) {
    val width: Dp = widthFloat.dp
    val height: Dp = heightFloat.dp

    val backgroundColor: Color = Color(backgroundColorLong)
}

//class DpMoshiAdapter {
//    @ToJson
//    fun toJson(dp: Dp): String = dp.value.toString()
//
//    @FromJson
//    fun fromJson(input: String): Dp = input.toFloat().dp
//}
//
//class ColorMoshiAdapter {
//    @ToJson
//    fun toJson(color: Color): String = color.value.toString()
//
//    @FromJson
//    fun fromJson(input: String): Color = Color(input.toLong())
//}