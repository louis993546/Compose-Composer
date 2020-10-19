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
interface Node {
    /**
     * Every Node needs to be able to render itself. The entry point is [Renderer]
     */
    @Composable
    fun Render()

    /**
     * [level] = how many indentation needed
     */
    @Composable
    fun Summarize(level: Int)
}

abstract class NodeWithChildren : Node {
    abstract val children: List<Node>

    @Composable
    abstract fun SummarizeTitle()

    @Composable
    override fun Summarize(level: Int) {
        Row(
            modifier = Modifier
                .padding(
                    start = (8 * level).dp,
                    end = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .clickable(onClick = { })
        ) {
            Icon(
                modifier = Modifier.size(16.dp).align(Alignment.CenterVertically),
                asset = vectorResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24)
            )
            SummarizeTitle()
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            val newLevel = level + 1
            children.forEach { child -> child.Summarize(level = newLevel) }
        }
    }
}

abstract class NodeWithoutChildren : Node {
    @Composable
    abstract fun SummarizeTitle()

    @Composable
    override fun Summarize(level: Int) {
        Box(
            modifier = Modifier.padding(
                start = ((8 * level) + 16).dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 8.dp
            )
        ) {
            SummarizeTitle()
        }
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