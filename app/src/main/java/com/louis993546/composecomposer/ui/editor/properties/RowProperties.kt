package com.louis993546.composecomposer.ui.editor.properties

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.louis993546.composecomposer.R
import com.louis993546.composecomposer.data.model.Node

@Composable
fun RowProperties(
    modifier: Modifier = Modifier,
    node: Node.Row,
    onNodeModified: (Node) -> Unit,
) {
    Column(modifier = modifier) {
        AddItemButton(node, onNodeModified)
        node.children.forEach { child ->
            // TODO should I just reuse NodeDescription here?
            Text(text = child.toString())
            AddItemButton(node, onNodeModified)
        }
    }
}

@Composable
private fun AddItemButton(node: Node.Row, onNodeModified: (Node) -> Unit) {
    Button(
        onClick = {
            onNodeModified(node) // TODO actually do something with the node
        },
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_add_24),
            contentDescription = "Add child"
        )
    }
}
