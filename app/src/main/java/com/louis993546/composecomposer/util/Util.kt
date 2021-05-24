package com.louis993546.composecomposer.util

import androidx.compose.runtime.Composable
import com.louis993546.composecomposer.data.model.Id
import com.louis993546.composecomposer.ui.theme.ComposeComposerTheme
import kotlin.random.Random

// TODO it'd be nice i can make sure it never collide
fun randId(): Id = Id(Random.nextInt())

val <T> T.exhaustive: T
    get() = this

/**
 * TODO see what makes sense for a Preview Theme
 */
@Composable
fun PreviewTheme(content: @Composable () -> Unit,) {
    ComposeComposerTheme(content = content)
}