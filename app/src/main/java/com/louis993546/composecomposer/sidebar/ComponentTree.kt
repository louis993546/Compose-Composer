package com.louis993546.composecomposer.sidebar

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.unit.dp
import com.louis993546.composecomposer.model.Component

@Composable
fun ComponentTree(modifier: Modifier = Modifier, layer: Int, component: Component) {
    Column(modifier = modifier) {
        ComponentChip(
                modifier = Modifier.padding(start = (layer * 16).dp),
                name = component.meaningfulName()
        )
        // TODO find a better way to do this
        if (component.hasChildren) {
            val newLayer = layer + 1
            (component as? Component.Row)?.children?.forEach {
                ComponentTree(layer = newLayer, component = it)
            }
            (component as? Component.Column)?.children?.forEach {
                ComponentTree(layer = newLayer, component = it)
            }
        }
    }
}