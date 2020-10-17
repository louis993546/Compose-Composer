package com.louis993546.composecomposer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import com.louis993546.composecomposer.Node.*
import com.louis993546.composecomposer.ui.ComposeComposerTheme
import com.luca992.compose.image.CoilImage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeComposerTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Renderer(
                        node = ColumnNode(
                            children = listOf(
                                TextNode("Testing 1"),
                                TextNode("Testing 2"),
                                TextNode("Testing 3"),
                                TextNode("Testing 4"),
                                TextNode("Testing 5"),
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeComposerTheme {
        Greeting("Android")
    }
}

@Composable
fun Builder() {

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

    data class TextNode(
        val text: String,
        val modifier: Modifier = Modifier,
    ) : Node() {
        @Composable
        override fun render() {
            Text(text = text, modifier = modifier)
        }
    }

    data class RowNode(
        val modifier: Modifier = Modifier,
        val children: List<Node>,
    ) : Node() {
        @Composable
        override fun render() {
            Row(modifier) {
                children.forEach { child -> Renderer(node = child) }
            }
        }
    }

    data class ColumnNode(
        val modifier: Modifier = Modifier,
        val children: List<Node>,
    ) : Node() {
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
        @Composable
        override fun render() {
            CoilImage(model = model, modifier = modifier)
        }
    }
}