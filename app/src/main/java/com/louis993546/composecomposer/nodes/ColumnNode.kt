package com.louis993546.composecomposer.nodes

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louis993546.composecomposer.Renderer

data class ColumnNode(
    val modifier: Modifier = Modifier,
    override val children: List<Node>,
) : NodeWithChildren() {
    @Composable
    override fun Render() {
        Column(modifier) {
            children.forEach { child -> Renderer(node = child) }
        }
    }

    @Composable
    override fun SummarizeTitle(modifier: Modifier) {
        TwoTextSummary(
            modifier = modifier,
            main = "Column",
            secondary = "length = ${children.size}"
        )
    }
}