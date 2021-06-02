package com.louis993546.composecomposer.data.adapter

import androidx.compose.ui.graphics.Color
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import timber.log.Timber

class ColorMoshiAdapter : JsonAdapter<Color?>() {
    override fun fromJson(reader: JsonReader): Color? {
        val long = reader.nextLong().toULong()
        val color = Color(long)
        Timber.tag("color").d(color.toString())
        return color
    }

    @ExperimentalUnsignedTypes
    override fun toJson(writer: JsonWriter, value: Color?) {
        Timber.tag("color").d(value?.toString())
        value?.value?.toLong()?.let {
            writer.value(it)
        }
    }
}
