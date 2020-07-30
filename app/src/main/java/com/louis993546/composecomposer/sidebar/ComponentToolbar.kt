package com.louis993546.composecomposer.sidebar

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.layout.Row

@Composable
fun ComponentToolbar() {
    Row {
        Text(text = "Row")
        Text(text = "Column")
        Text(text = "Text")
        Text(text = "Text Button")
    }
}
