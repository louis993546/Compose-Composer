package com.louis993546.composecomposer.ui.editor.properties

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louis993546.composecomposer.data.model.Node

@Composable
fun Properties(
    modifier: Modifier = Modifier,
    node: Node?,
    onNodeModified: (Node) -> Unit,
) {
    Box(modifier = modifier) {
        when (node) {
            is Node.Text -> TextProperties(
                node = node,
                onNodeModified = onNodeModified,
            )
            is Node.Image -> ImageProperties(
                node = node,
                onNodeModified = onNodeModified,
            )
            is Node.Row -> RowProperties(
                node = node,
                onNodeModified = onNodeModified,
            )
            else -> Text(text = "Properties. Currently selected node = ${node?.toString() ?: "empty"}")
        }
    }
}
