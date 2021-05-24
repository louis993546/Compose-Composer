/* (C)2021 */
package com.louis993546.composecomposer

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.data.adapter.ColorMoshiAdapter
import com.louis993546.composecomposer.data.adapter.DpMoshiAdapter
import com.louis993546.composecomposer.data.model.Node
import com.louis993546.composecomposer.data.model.Page
import com.louis993546.composecomposer.ui.properties.Properties
import com.louis993546.composecomposer.ui.renderer.PageRenderer
import com.louis993546.composecomposer.ui.theme.ComposeComposerTheme
import com.louis993546.composecomposer.ui.tree.Tree
import com.louis993546.composecomposer.util.exhaustive
import com.louis993546.composecomposer.util.randId
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import timber.log.Timber
import kotlin.time.measureTimedValue

class MainActivity : ComponentActivity() {
    companion object {
        // TODO this should be store in settings
        private val panelOrders = listOf(Panel.Tree, Panel.Renderer, Panel.Properties)
    }

    @ExperimentalStdlibApi
    private val moshi =
        Moshi.Builder().addAdapter(DpMoshiAdapter()).addAdapter(ColorMoshiAdapter()).build()

    private val defaultPage =
        Page(
            id = randId(),
            name = "",
            width = 360.dp,
            height = 640.dp,
            backgroundColor = Color.Gray,
            node =
            Node.Column(
                children =
                listOf(
                    Node.Text(text = "text 1"),
                    Node.Text(text = "text 2"),
                    Node.Image(
                        url =
                        "https://pbs.twimg.com/profile_images/1377946002473254912/h3q66L7m_400x400.png",
                        contentDescription = "Avatar of @louis993546 on Twitter",
                    ),
                    Node.Text(text = "text 3"),
                    Node.TextButton(text = "Button"),
                    Node.Text(text = "text 4"),
                    Node.Row(
                        children =
                        listOf(
                            Node.Text(text = "text 1"),
                            Node.Column(
                                children =
                                listOf(
                                    Node.Text(text = "text 1"),
                                    Node.Row(
                                        children =
                                        listOf(
                                            Node.Text(text = "text 1"),
                                            Node.Column(
                                                children =
                                                listOf(
                                                    Node.Text(
                                                        text = "text 1",
                                                    ),
                                                    Node.Row(
                                                        children =
                                                        listOf(
                                                            Node.Text(
                                                                text =
                                                                "text 1",
                                                            ),
                                                        ),
                                                    ),
                                                ),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                            Node.Text(text = "text 2"),
                            Node.Text(text = "text 3"),
                        ),
                    ),
                    Node.Checkbox(text = "Checkbox", checked = false),
                ),
            ),
        )

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var page by remember { mutableStateOf(defaultPage) }

            ComposeComposerTheme {
                Scaffold(
                    topBar = {
                        TopBar(testOnClick = {
                            Timber.d(
                                moshi.adapter(Page::class.java).toJson(page)
                            )
                        })
                    },
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colors.background,
                    ) {
                        Body(
                            panels = panelOrders,
                            page = page,
                            updateNode = { newNode ->
                                page = page.copyWithNewNode(newNode)
                                newNode
                            },
                        )
                    }
                }
            }
        }
    }

    private fun Page.copyWithNewNode(newNode: Node): Page =
        this.copy(node = this.node.copyWithNewNode(newNode))

    private fun Node.copyWithNewNode(newNode: Node): Node =
        when {
            this.id == newNode.id -> newNode
            this is Node.Column -> copy(children = children.map { it.copyWithNewNode(newNode) })
            this is Node.Row -> copy(children = children.map { it.copyWithNewNode(newNode) })
            else -> this
        }

    /**
     * (Hopefully) Slightly more efficient way to recursively way to replace a single node, by
     * skipping the rest once a single replacement has been done
     *
     * Commented out because it is often slower (benchmark via [measureTimedValue])
     */
    //  @OptIn(ExperimentalTime::class)
    //  private fun recursivelyCopyWithNewNode(children: List<Node>, newNode: Node): List<Node> {
    //    var currentIndex = 0
    //    var replacementHasBeenDone = false
    //    val newChildren = mutableListOf<Node>()
    //    while (currentIndex < children.size && !replacementHasBeenDone) {
    //      val currentNode = children[currentIndex]
    //      val updatedNode = currentNode.copyWithNewNode(newNode)
    //      if (currentNode == updatedNode) replacementHasBeenDone = true
    //      newChildren.add(updatedNode)
    //      currentIndex += 1
    //    }
    //    newChildren.addAll(children.takeLast(children.size - currentIndex))
    //    return newChildren
    //  }
}

@Composable
fun Body(
    modifier: Modifier = Modifier,
    panels: List<Panel>,
    page: Page,
    updateNode: (Node) -> Node,
) {
    var currentlySelectedNode by remember { mutableStateOf<Node?>(null) }

    val config = LocalConfiguration.current
    when {
        config.isPhoneSize() -> Text("TODO", modifier = modifier)
        config.isPhabletSize() -> Text("TODO", modifier = modifier)
        config.isTabletSize() ->
            TabletBody(
                modifier = modifier,
                panels = panels,
                page = page,
                currentlySelectedNode = currentlySelectedNode,
                onNodeSelected = { node -> currentlySelectedNode = node },
                onNodeModified = { newNode -> currentlySelectedNode = updateNode(newNode) },
            )
    }
}

@Composable
fun TabletBody(
    modifier: Modifier = Modifier,
    panels: List<Panel>,
    page: Page,
    currentlySelectedNode: Node?,
    onNodeSelected: (Node) -> Unit,
    onNodeModified: (Node) -> Unit,
) {
    Row(modifier = modifier) {
        panels.forEachIndexed { index, panel ->
            when (panel) {
                Panel.Tree ->
                    Page(
                        modifier = Modifier.weight(1f),
                        page = page,
                        onNodeSelected = onNodeSelected,
                    )
                Panel.Renderer ->
                    PageRenderer(
                        modifier = Modifier.weight(1f),
                        page = page,
                    )
                Panel.Properties ->
                    Properties(
                        modifier = Modifier.weight(1f),
                        node = currentlySelectedNode,
                        onNodeModified = onNodeModified,
                    )
            }.exhaustive
            if (index < panels.size) PanelDivider()
        }
    }
}

private fun Configuration.isPhoneSize(): Boolean = screenWidthDp <= 411

private fun Configuration.isPhabletSize(): Boolean = screenWidthDp <= 800

private fun Configuration.isTabletSize(): Boolean = screenWidthDp > 800

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

/** Copy of [androidx.compose.material.Divider] but vertical */
@Composable
fun PanelDivider(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
        modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
    )
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    testOnClick: () -> Unit,
) {
    TopAppBar(modifier = modifier) {
        Text("Composer")
        Button(onClick = testOnClick) { Text("Serialize to logcat") }
    }
}

enum class Panel {
    Tree,
    Renderer,
    Properties
}
