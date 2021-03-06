@file:Suppress("DEPRECATION")

package com.louis993546.composecomposer.data.model

import com.louis993546.composecomposer.util.randId
import com.squareup.moshi.JsonClass
import dev.zacsweers.moshix.sealed.annotations.DefaultNull
import dev.zacsweers.moshix.sealed.annotations.TypeLabel

// https://youtrack.jetbrains.com/issue/KT-41159
// TODO share this with the ticket
// typealias Id = Int
//@Deprecated(
//    message = "why is this so hard to use id",
//    replaceWith = ReplaceWith("Int")
//)
//inline class Id(val id: Int)

@DefaultNull
@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed class Node {
    abstract val id: Int

    @TypeLabel("text")
    @JsonClass(generateAdapter = true)
    data class Text(
        override val id: Int = randId(),
        val text: String,
    ) : Node()

    @TypeLabel("image")
    @JsonClass(generateAdapter = true)
    data class Image(
        override val id: Int = randId(),
        val url: String,
        val contentDescription: String,
    ) : Node()

    @TypeLabel("row")
    @JsonClass(generateAdapter = true)
    data class Row(
        override val id: Int = randId(),
        val children: List<Node>,
    ) : Node()

    @TypeLabel("column")
    @JsonClass(generateAdapter = true)
    data class Column(
        override val id: Int = randId(),
        val children: List<Node>,
    ) : Node()

    @TypeLabel("checkbox")
    @JsonClass(generateAdapter = true)
    data class Checkbox(
        override val id: Int = randId(),
        val text: String,
        val checked: Boolean,
    ) : Node()

    @TypeLabel("radio_group")
    @JsonClass(generateAdapter = true)
    data class RadioGroup(
        override val id: Int = randId(),
        val options: List<String>,
        val selection: Int?,
    ) : Node()

    @TypeLabel("text_button")
    @JsonClass(generateAdapter = true)
    data class TextButton(
        override val id: Int = randId(),
        val text: String,
    ) : Node()
}
