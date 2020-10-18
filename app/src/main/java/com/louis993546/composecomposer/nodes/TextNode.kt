package com.louis993546.composecomposer.nodes

import androidx.compose.foundation.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class TextNode(
    val text: String,
    val modifier: Modifier = Modifier,
) : Node() {
    @Composable
    override fun render() {
        Text(text = text, modifier = modifier)
    }

    @Composable
    override fun summary() {
        TwoTextSummary(main = "Text", secondary = text)
    }
}