package com.louis993546.composecomposer.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.squareup.moshi.JsonClass
import kotlinx.datetime.Instant

/**
 * TODO border color & thickness & radius nice to have: warning/hint when border color &
 *   [backgroundColor] are too similar
 */
@JsonClass(generateAdapter = true)
data class Page(
    val info: PageInfo,
    val width: Dp,
    val height: Dp,
    val backgroundColor: Color,
    val node: Node,
)

@JsonClass(generateAdapter = true)
data class PageInfo(
    val id: Int,
    val name: String,
    val lastUpdateAt: Instant?,
    val createdAt: Instant,
)
