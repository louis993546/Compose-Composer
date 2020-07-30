package com.louis993546.composecomposer.sidebar

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.unit.dp
import com.louis993546.composecomposer.model.Component

@Composable
fun ComponentTree(
        modifier: Modifier = Modifier,
        layer: Int = 0,
        component: Component
) {
    Column(modifier = modifier) {
        ComponentChip(
                modifier = Modifier.padding(
                        start = (layer * 32).dp,
                        bottom = 8.dp
                ),
                name = component.meaningfulName()
        )
        if (component is Component.NestedComponent) {
            val newLayer = layer + 1
            component.children.forEach {
                ComponentTree(layer = newLayer, component = it)
            }
        }
    }
}