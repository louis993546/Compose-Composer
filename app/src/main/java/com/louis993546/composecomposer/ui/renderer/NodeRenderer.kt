/* (C)2021 */
package com.louis993546.composecomposer.ui.renderer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.glide.rememberGlidePainter
import com.louis993546.composecomposer.data.model.Node
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
      is Node.Checkbox -> CheckboxNode(node = node)
      is Node.Column -> ColumnNode(node = node)
      is Node.Image -> ImageNode(node = node)
      is Node.RadioGroup -> RadioGroupNode(node = node)
      is Node.Row -> RowNode(node = node)
      is Node.Text -> TextNode(node = node)
    }.exhaustive
  }
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
