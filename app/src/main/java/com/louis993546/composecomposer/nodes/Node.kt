package com.louis993546.composecomposer.nodes

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.louis993546.composecomposer.NodeSelectionButton
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
    abstract fun SummarizeTitle(modifier: Modifier)

    @Composable
    override fun Summarize(level: Int) {
        // TODO need to save the value
        var expended by remember { mutableStateOf(false) }
        var dialogOpen by remember { mutableStateOf(false) }

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
                modifier = Modifier
                    .clickable(onClick = { expended = !expended })
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
                asset = vectorResource(
                    id = if (expended) R.drawable.ic_baseline_keyboard_arrow_down_24
                    else R.drawable.ic_baseline_keyboard_arrow_right_24
                )
            )
            SummarizeTitle(Modifier.weight(1f))
            Icon(
                modifier = Modifier.clickable(
                    onClick = { dialogOpen = true }
                ),
                asset = vectorResource (id = R.drawable.ic_baseline_add_24)
            )
        }
        if (expended) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                val newLevel = level + 1
                children.forEach { child -> child.Summarize(level = newLevel) }
            }
        }
        if (dialogOpen) {
            val closeDialog = { dialogOpen = false }
            Dialog(
                onDismissRequest = closeDialog,
                content = { NodeSelectionDialogContent() },
            )
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
fun TwoTextSummary(
    modifier: Modifier = Modifier,
    main: String,
    secondary: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
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

@Composable
fun NodeSelectionDialogContent() {
    val options = listOf(
        "Text" to R.drawable.ic_baseline_text_fields_24,
        "Image" to R.drawable.ic_baseline_image_24,
        "Row" to R.drawable.ic_baseline_view_row_24,
        "Column" to R.drawable.ic_baseline_table_column_24,
        "Scrollable Column" to R.drawable.ic_baseline_brightness_1_24,
    )
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(16.dp)
    ) {
        options.forEach { (text, icon) ->
            NodeSelectionButton(id = text, text = text, icon = icon, onClick = { id ->
                // TODO use callback to close the selection dialog + open something else
            })
        }
    }
}
