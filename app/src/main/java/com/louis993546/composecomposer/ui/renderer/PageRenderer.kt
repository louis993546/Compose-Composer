package com.louis993546.composecomposer.ui.renderer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louis993546.composecomposer.Page

@Composable
fun PageRenderer(
    modifier: Modifier,
    page: Page,
) {
    NodeRenderer(
        modifier = modifier
            .background(color = page.backgroundColor)
            .size(width = page.width, height = page.height), // TODO make it sit in the middle of the screen
        node = page.node,
    )
}