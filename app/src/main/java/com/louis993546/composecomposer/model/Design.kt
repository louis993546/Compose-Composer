package com.louis993546.composecomposer.model

import androidx.ui.foundation.Border
import androidx.ui.graphics.Color
import androidx.ui.unit.Dp
import androidx.ui.unit.dp

data class Design(
        val rootComponent: Component,
        val width: Dp = 360.dp,
        val height: Dp = 640.dp,
        val border: Border? = null,
        val backgroundColor: Color = Color.White
) {
    companion object {
        val dummy: Design get() = Design(
                rootComponent = Component.Column(
                        children = listOf(
                                Component.Text("add something from the side bar"),
                                Component.Row(
                                        children = listOf(
                                                Component.Text("1"),
                                                Component.Text("2"),
                                                Component.Text("3"),
                                                Component.Text("4"),
                                                Component.Text("5")
                                        )
                                )
                        )
                ),
                width = 360.dp,
                height = 640.dp,
                border = Border(2.dp, Color.Black),
                backgroundColor = Color(0xFFFAFAFA)
        )
    }
}
