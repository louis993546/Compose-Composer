package com.louis993546.composecomposer.ui.editor.renderer

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.louis993546.composecomposer.data.model.Node
import com.louis993546.composecomposer.util.PreviewTheme

@Composable
fun TextNode(
    node: Node.Text,
) {
    Text(
        text = node.text,
    )
}

@Preview
@Composable
fun Preview_TextNode() {
    PreviewTheme { TextNode(node = Node.Text(text = "text node")) }
}
