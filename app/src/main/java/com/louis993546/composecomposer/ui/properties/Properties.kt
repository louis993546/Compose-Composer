package com.louis993546.composecomposer.ui.properties

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louis993546.composecomposer.Node

@Composable
fun Properties(
    modifier: Modifier = Modifier,
    node: Node?,
    onNodeModified: (Node) -> Unit,
) {
    Box(modifier = modifier) {
        Text(
            text = "Properties. Currently selected node = ${node?.toString() ?: "empty"}"
        )
    }
}
