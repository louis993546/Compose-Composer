package com.louis993546.composecomposer.data.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import timber.log.Timber

class InstantMoshiAdapter : JsonAdapter<Instant?>() {
    /**
     * @suppress SwallowedException from detekt, because everything is still in dev
     *           TODO once the schema is more stable, see if there should be some way to handle this
     */
    @Suppress("SwallowedException")
    override fun fromJson(reader: JsonReader): Instant? = try {
        reader.nextString().toInstant()
    } catch (e: IllegalArgumentException) {
        Timber.e(e)
        null
    }

    override fun toJson(writer: JsonWriter, value: Instant?) {
        value?.toString()?.let {
            writer.value(it)
        } ?: writer.nullValue()
    }
}
