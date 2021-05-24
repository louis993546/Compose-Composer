/* (C)2021 */
package com.louis993546.composecomposer.ui.renderer

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.data.model.Page

/**
 * TODO make sure when it's scale is bigger than the panel, it don't cover up things in other panels
 */
@Composable
fun PageRenderer(
    modifier: Modifier,
    page: Page,
) {
    var scale by remember { mutableStateOf(1f) }
    val zoomState = rememberTransformableState { zoomChange, _, _ -> scale *= zoomChange }
    Column(
        modifier = modifier
          .widthIn(min = (page.width + 16.dp))
          .fillMaxHeight()
    ) {
        Text(text = "Scale: ${"%.2f".format(scale)}")

        NodeRenderer(
            modifier =
            Modifier
              .graphicsLayer(scaleX = scale, scaleY = scale)
              .transformable(zoomState)
              .size(width = page.width, height = page.height)
              .align(Alignment.CenterHorizontally)
              .background(color = page.backgroundColor),
            node = page.node,
        )
    }
}
