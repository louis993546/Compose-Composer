package com.louis993546.composecomposer.preview

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box

import com.louis993546.composecomposer.model.Component


@Composable
fun Preview(modifier: Modifier = Modifier, component: Component) {
    Box(modifier) {
        Render(component = component)
    }
}