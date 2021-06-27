package com.louis993546.composecomposer.ui.editor.properties

import androidx.compose.foundation.layout.Column
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louis993546.composecomposer.data.model.Node


@Composable
fun TextProperties(
    modifier: Modifier = Modifier,
    node: Node.Text,
    onNodeModified: (Node) -> Unit,
) {
    Column(modifier = modifier) {
        TextField(
            value = node.text,
            onValueChange = { newText -> onNodeModified(node.copy(text = newText)) },
        )
    }
}
