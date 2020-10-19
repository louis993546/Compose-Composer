package com.louis993546.composecomposer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
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
    var dialogOpen by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose Composer") },
                actions = {
                    Icon(
                        asset = vectorResource(id = R.drawable.ic_baseline_arrow_forward_24),
                        modifier = Modifier.clickable(onClick = nextPage)
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { dialogOpen = true },
                text = { Text(text = "Add node") },
                icon = { Icon(asset = vectorResource(id = R.drawable.ic_baseline_add_24)) },
            )
        },
    ) {
        ScrollableColumn { tree.Summarize(level = 0) }
    }

    if (dialogOpen) {
        val closeDialog = { dialogOpen = false }
        Timber.d("Dialog opening")
        AlertDialog(
            onDismissRequest = closeDialog,
            title = { Text("Dialog") },
            confirmButton = {
                Button(onClick = closeDialog) { Text(text = "Confirm") }
            },
            dismissButton = {
                Button(onClick = closeDialog) { Text(text = "Dismiss") }
            },
            text = { Text("Text") }
        )
    }
}

/**
 * See [Node] for what options do you have and how will things get render
 */
@Composable
fun Renderer(node: Node) {
    node.Render()
}

