package com.louis993546.composecomposer.nodes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.luca992.compose.image.CoilImage

data class ImageNode(
    val model: Any,
    val modifier: Modifier = Modifier,
) : NodeWithoutChildren() {
    @Composable
    override fun Render() {
        CoilImage(model = model, modifier = modifier)
    }

    @Composable
    override fun SummarizeTitle() {
        TwoTextSummary(main = "Image", secondary = "$model")
    }
}