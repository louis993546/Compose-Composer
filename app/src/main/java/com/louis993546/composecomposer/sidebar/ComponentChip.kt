package com.louis993546.composecomposer.sidebar

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.dp

// TODO learn drag and drop
@Composable
fun ComponentChip(modifier: Modifier = Modifier, name: String) {
    Box(
            modifier = modifier.padding(4.dp).fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            backgroundColor = MaterialTheme.colors.secondary
    ) {
        Text(
                modifier = Modifier.padding(4.dp),
                text = name,
                color = MaterialTheme.colors.onSecondary
        )
    }
}