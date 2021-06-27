package com.louis993546.composecomposer.util

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.ui.editor.theme.ComposeComposerTheme
import kotlin.random.Random

@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated(
    message =
    "This is only for development purpose. There will be separate id generator to allow" +
            "generating unique id in specific context (e.g. no id collision in node)",
)
fun randId(): Int = Random.nextInt()

/**
 * Page ID will be created by DB to ensure uniqueness, so this
 */
fun placeholderId(): Int = -1

fun Configuration.isPhoneSize(): Boolean = screenWidthDp <= 411

fun Configuration.isPhabletSize(): Boolean = screenWidthDp <= 800

fun Configuration.isTabletSize(): Boolean = screenWidthDp > 800


/** It's just the normal theme ([ComposeComposerTheme]) with a border around it */
@Composable
fun PreviewTheme(
    content: @Composable () -> Unit,
) {
    Box(Modifier.border(2.dp, Color.Black)) { ComposeComposerTheme(content = content) }
}
