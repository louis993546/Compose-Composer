/* (C)2021 */
package com.louis993546.composecomposer.ui.tree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.R
import com.louis993546.composecomposer.data.model.Node
import com.louis993546.composecomposer.util.exhaustive

/**
 * TODO
 * - icons for each type of node instead of name in text
 */
@Composable
fun Tree(
    modifier: Modifier = Modifier,
    node: Node,
    onNodeSelected: (Node) -> Unit,
) {
  Box(modifier = modifier) {
    when (node) {
      is Node.Checkbox -> NodeDescription(
          modifier = Modifier.clickable { onNodeSelected(node) },
          icon = R.drawable.ic_baseline_check_box_outline_blank_24,
          contentDescription = "Check box",
          text = "${node.text} = ${node.checked}")
      is Node.Column -> Column(
          modifier = Modifier.clickable { onNodeSelected(node) },
      ) {
        NodeDescription(
            icon = R.drawable.ic_baseline_view_column_24,
            contentDescription = "Row",
            text = "${node.children.size} items")
        node.children.forEach { child ->
          Tree(
              modifier = Modifier.padding(start = 16.dp),
              node = child,
              onNodeSelected = onNodeSelected,
          )
        }
      }
      is Node.Image -> NodeDescription(
          modifier = Modifier.clickable { onNodeSelected(node) },
          icon = R.drawable.ic_baseline_image_24,
          contentDescription = "Image",
          text = "(TODO some description)")
      is Node.RadioGroup -> NodeDescription(
          modifier = Modifier.clickable { onNodeSelected(node) },
          icon = R.drawable.ic_baseline_radio_button_checked_24,
          contentDescription = "Radio Group",
          text = "(TODO some description)")
      is Node.Row -> Column(
          modifier = Modifier.clickable { onNodeSelected(node) },
      ) {
        NodeDescription(
            icon = R.drawable.ic_baseline_table_rows_24,
            contentDescription = "Row",
            text = "${node.children.size} items")
        node.children.forEach { child ->
          Tree(
              modifier = Modifier.padding(start = 16.dp),
              node = child,
              onNodeSelected = onNodeSelected,
          )
        }
      }
      is Node.Text -> NodeDescription(
          modifier = Modifier.clickable { onNodeSelected(node) },
          icon = R.drawable.ic_baseline_text_fields_24,
          contentDescription = "Text",
          text = node.text)
      is Node.TextButton -> NodeDescription(
          icon = R.drawable.ic_baseline_touch_app_24,
          contentDescription = "Text Button",
          text = node.text
      )
    }.exhaustive
  }
}
