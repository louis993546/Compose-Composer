package com.louis993546.composecomposer.nodes

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louis993546.composecomposer.Renderer

data class ColumnNode(
    val modifier: Modifier = Modifier,
    override val children: List<Node>,
) : Node() {
    @Composable
    override fun render() {
        Column(modifier) {
            children.forEach { child -> Renderer(node = child) }
        }
    }

    @Composable
    override fun summary() {
        TwoTextSummary(main = "Column", secondary = "length = ${children.size}")
    }
}