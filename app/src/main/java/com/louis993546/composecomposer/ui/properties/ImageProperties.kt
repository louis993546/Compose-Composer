package com.louis993546.composecomposer.ui.properties

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.imageloading.isFinalState
import com.louis993546.composecomposer.data.model.Node
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter


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
