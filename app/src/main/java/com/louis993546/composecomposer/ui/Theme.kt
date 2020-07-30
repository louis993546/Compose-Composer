package com.louis993546.composecomposer.ui

import androidx.compose.Composable
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.material.MaterialTheme
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette

private val DarkColorPalette = darkColorPalette(
        primary = green300,
        primaryVariant = green500,
        secondary = deepPurple300,
        background = gray900,
        surface = gray800,
        onPrimary = black,
        onSecondary = black,
        onBackground = white,
        onSurface = white
)

private val LightColorPalette = lightColorPalette(
        primary = green300,
        primaryVariant = green500,
        secondary = deepPurple300,
        background = gray50,
        surface = gray200,
        onPrimary = black,
        onSecondary = black,
        onBackground = black,
        onSurface = black
)

@Composable
fun ComposeComposerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
    )
}