/* (C)2021 */
package com.louis993546.composecomposer.data.adapter

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class DpMoshiAdapter : JsonAdapter<Dp?>() {
  override fun fromJson(reader: JsonReader): Dp? = reader.nextDouble().toFloat().dp

  override fun toJson(writer: JsonWriter, value: Dp?) {
    value?.let { writer.value(it.value) }
  }
}
