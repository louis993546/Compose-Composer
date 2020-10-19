package com.louis993546.composecomposer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.louis993546.composecomposer.nodes.*
import com.louis993546.composecomposer.ui.ComposeComposerTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private val pageFlow = MutableStateFlow(Page.Builder)

    //     private val initialNode = EmptyNode
    private val initialNode = ScrollableColumnNode(
        children = listOf(
            TextNode("A"),
            TextNode("B"),
            TextNode("C"),
            ImageNode("https://avatars2.githubusercontent.com/u/3873011?s=60&u=e6a8b2a51ae37038bf1c71e218f8052a55763daa&v=4"),
            RowNode(
                children = listOf(
                    TextNode("A"),
                    TextNode("B"),
                    TextNode("C"),
                    ImageNode("https://avatars2.githubusercontent.com/u/3873011?s=60&u=e6a8b2a51ae37038bf1c71e218f8052a55763daa&v=4"),
                )
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeComposerTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val page by pageFlow.collectAsState()
                    var tree by state<Node> { initialNode }

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
            TopAppBar(title = { Text("Compose Composer") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = nextPage,
                icon = { Icon(asset = vectorResource(id = R.drawable.ic_baseline_arrow_forward_24)) },
            )
        },
    ) {
        ScrollableColumn { tree.Summarize(level = 0) }
    }
}



@Composable
fun NodeSelectionButton(
    id: String,
    text: String,
    icon: Int,
    onClick: (String) -> Unit
) {
    Button(
        modifier = Modifier.padding(4.dp),
        onClick = { onClick(id) },
    ) {
        Icon(vectorResource(id = icon))
        Text(text, modifier = Modifier.padding(start = 4.dp))
    }
}

/**
 * See [Node] for what options do you have and how will things get render
 */
@Composable
fun Renderer(node: Node) {
    node.Render()
}

