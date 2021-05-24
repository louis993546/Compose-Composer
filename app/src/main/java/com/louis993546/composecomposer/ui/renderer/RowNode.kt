package com.louis993546.composecomposer.ui.renderer

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.louis993546.composecomposer.data.model.Node
import com.louis993546.composecomposer.util.PreviewTheme

@Composable
fun RowNode(
    node: Node.Row,
) {
    Row {
        node.children.forEach { child ->
            NodeRenderer(node = child, rowScope = this)
        }
    }
}

@Preview
@Composable
fun Preview_RowNode() {
    PreviewTheme {
        RowNode(node = Node.Row(children = listOf(
            Node.Text(text = "text 1"),
            Node.Text(text = "text 2"),
            Node.Text(text = "text 3"),
        )))
    }
}
