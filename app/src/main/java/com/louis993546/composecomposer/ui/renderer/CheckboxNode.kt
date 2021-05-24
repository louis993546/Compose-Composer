package com.louis993546.composecomposer.ui.renderer

import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.louis993546.composecomposer.data.model.Node
import com.louis993546.composecomposer.util.PreviewTheme

@Composable
fun CheckboxNode(
    node: Node.Checkbox,
) {
    Checkbox(
        checked = node.checked,
        onCheckedChange = { TODO("need to modify this node, probably by propagate it back up") },
    )
}

@Preview
@Composable
fun Preview_CheckboxNode() {
    PreviewTheme {
        CheckboxNode(node = Node.Checkbox(
            text = "checkbox",
            checked = true,
        ))
    }
}
