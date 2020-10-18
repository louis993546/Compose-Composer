package com.louis993546.composecomposer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.Node.*
import com.louis993546.composecomposer.ui.ComposeComposerTheme
import com.luca992.compose.image.CoilImage
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
                            nextPage = { this.pageFlow.value = Page.Renderer },
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

/**
 * Node are a set of pre-define component that Compose has.
 */
sealed class Node {
    /**
     * Every Node needs to be able to render itself. The entry point is [Renderer]
     */
    @Composable
    abstract fun render()

    abstract val nodeName: String
    open val children: List<Node>? = null

    @Composable
    fun summarize() {
        SummarizeText(nodeName = nodeName, children = children)
    }

    object EmptyNode : Node() {
        override val nodeName = "Empty"

        @Composable
        override fun render() {
            Box(
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center,
            ) {
                Text("There is nothing yet. Click the plus icon to start!")
            }
        }
    }

    data class TextNode(
        val text: String,
        val modifier: Modifier = Modifier,
    ) : Node() {
        override val nodeName = "Text ($text)"

        @Composable
        override fun render() {
            Text(text = text, modifier = modifier)
        }
    }

    data class RowNode(
        val modifier: Modifier = Modifier,
        override val children: List<Node>,
    ) : Node() {
        override val nodeName = "Row (${children.size})"

        @Composable
        override fun render() {
            Row(modifier) {
                children.forEach { child -> Renderer(node = child) }
            }
        }
    }

    data class ColumnNode(
        val modifier: Modifier = Modifier,
        override val children: List<Node>,
    ) : Node() {
        override val nodeName = "Column (${children.size})"

        @Composable
        override fun render() {
            Column(modifier) {
                children.forEach { child -> Renderer(node = child) }
            }
        }
    }

    data class ImageNode(
        val model: Any,
        val modifier: Modifier = Modifier,
    ) : Node() {
        override val nodeName = "Image ($model)"

        @Composable
        override fun render() {
            CoilImage(model = model, modifier = modifier)
        }
    }

    @Composable
    fun SummarizeText(nodeName: String, children: List<Node>? = null) {
        Row {
            val size = Modifier.size(16.dp)
            if (children == null) Spacer(modifier = size)
            else Icon(
                modifier = size.align(Alignment.CenterVertically),
                asset = vectorResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24)
            )
            Text(text = nodeName, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            children?.forEach { child -> child.summarize() }
        }
    }
}