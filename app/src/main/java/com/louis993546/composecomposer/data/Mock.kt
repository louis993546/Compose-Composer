package com.louis993546.composecomposer.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.data.model.Node
import com.louis993546.composecomposer.data.model.Page
import com.louis993546.composecomposer.data.model.PageInfo
import com.louis993546.composecomposer.util.randId
import kotlinx.datetime.Clock

val defaultPage =
    Page(
        info = PageInfo(
            id = randId(),
            name = "default page",
            createdAt = Clock.System.now(),
            lastUpdateAt = null
        ),
        width = 360.dp,
        height = 640.dp,
        backgroundColor = Color.Gray,
        node =
        Node.Column(
            children =
            listOf(
                Node.Text(text = "text 1"),
                Node.Text(text = "text 2"),
                Node.Image(
                    url =
                    "https://pbs.twimg.com/profile_images/1377946002473254912/h3q66L7m_400x400.png",
                    contentDescription = "Avatar of @louis993546 on Twitter",
                ),
                Node.Text(text = "text 3"),
                Node.TextButton(text = "Button"),
                Node.Text(text = "text 4"),
                Node.Row(
                    children =
                    listOf(
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Text(text = "text 1"),
                        Node.Column(
                            children =
                            listOf(
                                Node.Text(text = "text 1"),
                                Node.Row(
                                    children =
                                    listOf(
                                        Node.Text(text = "text 1"),
                                        Node.Column(
                                            children =
                                            listOf(
                                                Node.Text(
                                                    text = "text 1",
                                                ),
                                                Node.Row(
                                                    children =
                                                    listOf(
                                                        Node.Text(
                                                            text =
                                                            "text 1",
                                                        ),
                                                    ),
                                                ),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                        Node.Text(text = "text 2"),
                        Node.Text(text = "text 3"),
                    ),
                ),
                Node.Checkbox(text = "Checkbox", checked = false),
            ),
        ),
    )
