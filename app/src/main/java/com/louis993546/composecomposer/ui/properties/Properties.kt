package com.louis993546.composecomposer.ui.properties

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.imageloading.isFinalState
import com.louis993546.composecomposer.data.model.Node
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

@Composable
fun Properties(
    modifier: Modifier = Modifier,
    node: Node?,
    onNodeModified: (Node) -> Unit,
) {
    Box(modifier = modifier) {
        when (node) {
            is Node.Text ->
                TextProperties(
                    node = node,
                    onNodeModified = onNodeModified,
                )
            is Node.Image ->
                ImageProperties(
                    node = node,
                    onNodeModified = onNodeModified,
                )
            else -> Text(text = "Properties. Currently selected node = ${node?.toString() ?: "empty"}")
        }
    }
}

@Composable
fun TextProperties(
    modifier: Modifier = Modifier,
    node: Node.Text,
    onNodeModified: (Node) -> Unit,
) {
    Column(modifier = modifier) {
        TextField(
            value = node.text,
            onValueChange = { newText -> onNodeModified(node.copy(text = newText)) },
        )
    }
}

@Composable
fun ImageProperties(
    modifier: Modifier = Modifier,
    node: Node.Image,
    onNodeModified: (Node) -> Unit,
) {
    var newUrl by remember { mutableStateOf(node.url) }
    val painter = rememberGlidePainter(request = newUrl)
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(painter) {
        snapshotFlow { painter.loadState }.filter { it.isFinalState() }.collect { result ->
            if (result is ImageLoadState.Success) {
                error = null
                onNodeModified(node.copy(url = newUrl))
            } else if (result is ImageLoadState.Error) {
                result.throwable
                error = result.throwable?.stackTraceToString() ?: "Some error"
            }
        }
    }

    Column(modifier = modifier) {
        TextField(
            label = { Text(text = "URL") },
            value = newUrl,
            singleLine = true,
            onValueChange = { newUrl = it },
            isError = error != null,
        )
        TextField(
            label = { Text(text = "Alt (Optional)") },
            value = node.contentDescription,
            singleLine = true,
            onValueChange = { onNodeModified(node.copy(contentDescription = it)) },
        )

        Divider()
        Text(text = "Preview")

        if (error != null) {
            Text(text = error!!)
        }

        Image(
            painter = painter,
            contentDescription = node.contentDescription,
        )
    }
}
