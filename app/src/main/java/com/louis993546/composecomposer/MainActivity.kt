package com.louis993546.composecomposer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import com.louis993546.composecomposer.nodes.ColumnNode
import com.louis993546.composecomposer.nodes.ImageNode
import com.louis993546.composecomposer.nodes.Node
import com.louis993546.composecomposer.nodes.TextNode
import com.louis993546.composecomposer.ui.ComposeComposerTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private val pageFlow = MutableStateFlow(Page.Builder)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeComposerTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val page by pageFlow.collectAsState()
//                    var tree by state<Node> { Node.EmptyNode }
                    var tree by state<Node> {
                        ColumnNode(
                            children = listOf(
                                TextNode("A"),
                                TextNode("B"),
                                TextNode("C"),
                                ImageNode("https://avatars2.githubusercontent.com/u/3873011?s=60&u=e6a8b2a51ae37038bf1c71e218f8052a55763daa&v=4")
                            )
                        )
                    }

                    when (page) {
                        Page.Builder -> Builder(
                            tree = tree,
                            nextPage = {
                                this.pageFlow.value = Page.Renderer
                            },
                        )
                        Page.Renderer -> Renderer(tree)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (pageFlow.value == Page.Renderer) {
            pageFlow.value = Page.Builder
        } else {
            super.onBackPressed()
        }
    }
}

enum class Page {
    Builder, Renderer
}

@Composable
fun Builder(nextPage: () -> Unit, tree: Node) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose Composer") },
                actions = {
                    // TODO save, open, and export
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = nextPage) {
                Icon(asset = vectorResource(id = R.drawable.ic_baseline_arrow_forward_24))
            }
        },
    ) {
        Column {
            tree.summarize()
        }
    }
}

/**
 * See [Node] for what options do you have and how will things get render
 */
@Composable
fun Renderer(node: Node) {
    node.render()
}

