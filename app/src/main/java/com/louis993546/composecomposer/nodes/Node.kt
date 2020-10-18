package com.louis993546.composecomposer.nodes

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.R

/**
 * Node are a set of pre-define component that Compose has.
 */
abstract class Node {
    /**
     * Every Node needs to be able to render itself. The entry point is [Renderer]
     */
    @Composable
    abstract fun render()

    @Composable
    abstract fun summary()

    open val children: List<Node>? = null

    @Composable
    fun summarize() {
        SummarizeText(children = children)
    }

    @Composable
    fun SummarizeText(children: List<Node>? = null) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = {

                })
        ) {
            val size = Modifier.size(16.dp)
            if (children == null) Spacer(modifier = size)
            else Icon(
                modifier = size.align(Alignment.CenterVertically),
                asset = vectorResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24)
            )
            summary()
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            children?.forEach { child -> child.summarize() }
        }
    }

    @Composable
    fun TwoTextSummary(main: String, secondary: String) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(main)
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = secondary,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}