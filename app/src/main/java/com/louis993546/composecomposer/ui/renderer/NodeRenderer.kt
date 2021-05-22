package com.louis993546.composecomposer.ui.renderer

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louis993546.composecomposer.Node
import com.louis993546.composecomposer.util.exhaustive

@Composable
fun NodeRenderer(
    modifier: Modifier = Modifier,
    node: Node,
    rowScope: RowScope? = null,
    columnScope: ColumnScope? = null,
) {
    Box(modifier = modifier) {
        when (node) {
            is Node.Checkbox -> Checkbox(
                checked = node.checked,
                onCheckedChange = { TODO("need to modify this node, probably by propagate it back up") },
            )
            is Node.Column -> Column {
                node.children.forEach { child ->
                    NodeRenderer(node = child, columnScope = this)
                }
            }
            is Node.Image -> Text(
                text = "TODO image", // TODO
            )
            is Node.RadioGroup -> Text(
                text = "TODO radio group",
            )
            is Node.Row -> Row {
                node.children.forEach { child ->
                    NodeRenderer(node = child, rowScope = this)
                }
            }
            is Node.Text -> Text(
                text = node.text,
            )
        }.exhaustive
    }
}