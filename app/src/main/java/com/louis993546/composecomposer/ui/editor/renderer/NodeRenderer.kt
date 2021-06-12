package com.louis993546.composecomposer.ui.editor.renderer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.exhaustive.Exhaustive
import com.google.accompanist.glide.rememberGlidePainter
import com.louis993546.composecomposer.data.model.Node

@Composable
fun NodeRenderer(
    modifier: Modifier = Modifier,
    node: Node,
    rowScope: RowScope? = null,
    columnScope: ColumnScope? = null,
) {
    Box(modifier = modifier) {
        @Exhaustive
        when (node) {
            is Node.Checkbox -> CheckboxNode(node = node)
            is Node.Column -> ColumnNode(node = node)
            is Node.Image -> ImageNode(node = node)
            is Node.RadioGroup -> RadioGroupNode(node = node)
            is Node.Row -> RowNode(node = node)
            is Node.Text -> TextNode(node = node)
            is Node.TextButton -> TextButtonNode(node = node)
        }
    }
}

@Composable
private fun TextButtonNode(
    node: Node.TextButton,
) {
    Button(
        onClick = {},
        content = { Text(text = node.text) },
    )
}

@Composable
private fun ImageNode(
    node: Node.Image,
) {
    Image(
        painter = rememberGlidePainter(request = node.url),
        contentDescription = "",
    )
}

@Composable
private fun RadioGroupNode(
    node: Node.RadioGroup,
) {
    Text(
        text = "TODO radio group",
    )
}
