package com.louis993546.composecomposer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.util.PreviewTheme

/** Copy of [androidx.compose.material.Divider] but vertical */
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
        modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
    )
}

@Preview
@Composable
fun Preview_VerticalDivider() {
    PreviewTheme {
        VerticalDivider()
    }
}
