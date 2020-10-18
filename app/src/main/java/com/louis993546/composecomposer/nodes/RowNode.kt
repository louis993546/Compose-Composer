package com.louis993546.composecomposer.nodes

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louis993546.composecomposer.Renderer

data class RowNode(
    val modifier: Modifier = Modifier,
    override val children: List<Node>,
) : Node() {
    @Composable
    override fun render() {
        Row(modifier) {
            children.forEach { child -> Renderer(node = child) }
        }
    }

    @Composable
    override fun summary() {
        TwoTextSummary(main = "Row", secondary = "length = ${children.size}")
    }
}