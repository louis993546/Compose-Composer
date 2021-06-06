package com.louis993546.composecomposer.ui.editor.renderer

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.louis993546.composecomposer.data.model.Node
import com.louis993546.composecomposer.util.PreviewTheme

@Composable
fun ColumnNode(
    node: Node.Column,
) {
    Column { node.children.forEach { child -> NodeRenderer(node = child, columnScope = this) } }
}

@Preview
@Composable
fun Preview_ColumnNode() {
    PreviewTheme {
        ColumnNode(
            node =
            Node.Column(
                children =
                listOf(
                    Node.Text(text = "text 1"),
                    Node.Text(text = "text 2"),
                    Node.Text(text = "text 3"),
                ),
            ),
        )
    }
}
