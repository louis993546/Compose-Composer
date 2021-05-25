package com.louis993546.composecomposer.data.adapter

import androidx.compose.ui.graphics.Color
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import timber.log.Timber

class ColorMoshiAdapter : JsonAdapter<Color?>() {
    override fun fromJson(reader: JsonReader): Color? {
        val colorLong = reader.nextLong()
        Timber.tag("qqq").d(colorLong.toString())
//        val x: ULong = colorLong.toULong() // TODO this needs kotlin 1.5
        return Color(colorLong)
    }

    @ExperimentalUnsignedTypes
    override fun toJson(writer: JsonWriter, value: Color?) {
        value?.value?.toLong()?.let {
            Timber.tag("qqq").d(it.toString())
            writer.value(it)
        }
    }
}
