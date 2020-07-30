package com.louis993546.composecomposer.preview

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.material.Button
import com.louis993546.composecomposer.model.Component

@Composable
fun Render(component: Component) {
    when (component) {
        is Component.Text -> Text(text = component.text)
        is Component.Row -> Row {
            component.children.forEach {
                Render(component = it)
            }
        }
        is Component.Column -> Column {
            component.children.forEach {
                Render(component = it)
            }
        }
        is Component.Button -> Button(onClick = {}) { Render(component = Component.Text(component.text)) }
    }
}