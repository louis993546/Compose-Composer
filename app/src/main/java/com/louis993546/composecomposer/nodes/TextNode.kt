package com.louis993546.composecomposer.nodes

import androidx.compose.foundation.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class TextNode(
    val text: String,
    val modifier: Modifier = Modifier,
) : NodeWithoutChildren() {
    @Composable
    override fun Render() {
        Text(text = text, modifier = modifier)
    }

    @Composable
    override fun SummarizeTitle() {
        TwoTextSummary(main = "Text", secondary = text)
    }
}