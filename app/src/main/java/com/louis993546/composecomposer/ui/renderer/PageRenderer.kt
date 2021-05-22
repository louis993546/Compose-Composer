package com.louis993546.composecomposer.ui.renderer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.Page

@Composable
fun PageRenderer(
    modifier: Modifier,
    page: Page,
) {
    Box(
        modifier = modifier
            .widthIn(min = (page.width + 16.dp))
            .fillMaxHeight()
    ) {
        NodeRenderer(
            modifier = Modifier
                .size(width = page.width, height = page.height)
                .scale(1f) // TODO expose this
                .background(color = page.backgroundColor)
                .align(Alignment.Center)
                ,
            node = page.node,
        )
    }
}