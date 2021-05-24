/* (C)2021 */
package com.louis993546.composecomposer.data.adapter

import androidx.compose.ui.graphics.Color
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class ColorMoshiAdapter : JsonAdapter<Color?>() {
  override fun fromJson(reader: JsonReader): Color? = Color(reader.nextLong())

  override fun toJson(writer: JsonWriter, value: Color?) {
    writer.value(value?.value?.toString())
  }
}
