package com.louis993546.composecomposer.nodes

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louis993546.composecomposer.Renderer

/**
 * TODO There's got to be a better way to do this
 */
data class ScrollableColumnNode(
    val modifier: Modifier = Modifier,
    override val children: List<Node>,
) : NodeWithChildren() {
    @Composable
    override fun Render() {
        ScrollableColumn(modifier) {
            children.forEach { child -> Renderer(node = child) }
        }
    }

        @Composable
    override fun SummarizeTitle(modifier: Modifier) {
        TwoTextSummary(
            modifier = modifier,
            main = "Scrollable Column",
            secondary = "length = ${children.size}"
        )
    }
}