/* (C)2021 */
package com.louis993546.composecomposer.util

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.data.model.Id
import com.louis993546.composecomposer.ui.theme.ComposeComposerTheme
import kotlin.random.Random

@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated(
    message =
    "This is only for development purpose. There will be separate id generator to allow" +
            "generating unique id in specific context (e.g. no id collision in node)",
)
fun randId(): Id = Id(Random.nextInt())

val <T> T.exhaustive: T
    get() = this

/** It's just the normal theme ([ComposeComposerTheme]) with a border around it */
@Composable
fun PreviewTheme(
    content: @Composable () -> Unit,
) {
    Box(Modifier.border(2.dp, Color.Black)) { ComposeComposerTheme(content = content) }
}
