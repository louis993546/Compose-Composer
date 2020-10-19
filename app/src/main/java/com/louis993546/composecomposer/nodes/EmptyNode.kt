package com.louis993546.composecomposer.nodes

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

object EmptyNode : NodeWithoutChildren() {
    @Composable
    override fun Render() {
        Box(
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
        ) {
            Text("There is nothing yet. Click the plus icon to start!")
        }
    }

    @Composable
    override fun SummarizeTitle() {
        Text("Empty")
    }
}