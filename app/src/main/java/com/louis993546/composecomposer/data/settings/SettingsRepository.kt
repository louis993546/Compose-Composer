package com.louis993546.composecomposer.data.settings

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.louis993546.composecomposer.PanelOrder
import com.louis993546.composecomposer.Settings
import com.louis993546.composecomposer.ui.editor.Panel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

interface SettingsRepository {
    suspend fun getPanelOrder(
        fallback: List<Panel> = emptyList(),
    ): List<Panel>

    fun getPanelOrderFlow(
        fallback: List<Panel> = emptyList(),
    ): Flow<List<Panel>>

    suspend fun savePanelOrder(
        panelOrder: List<Panel>,
    ) // TODO Result type
}

class DataStoreSettingsRepository(
    private val settingsStore: DataStore<Settings>,
) : SettingsRepository {
    override suspend fun getPanelOrder(fallback: List<Panel>): List<Panel> =
        getPanelOrderFlow(fallback).first()

    override fun getPanelOrderFlow(fallback: List<Panel>): Flow<List<Panel>> =
        settingsStore.data.map {
            mutableMapOf<Int, Panel>().apply {
                this[it.panelOrder.tree] = Panel.Tree
                this[it.panelOrder.renderer] = Panel.Renderer
                this[it.panelOrder.properties] = Panel.Properties
            }.map { it.value }
        }

    override suspend fun savePanelOrder(panelOrder: List<Panel>) {
        settingsStore.updateData { settings ->
            settings.toBuilder()
                .setPanelOrder(
                    PanelOrder.newBuilder()
                        .setTree(panelOrder.indexOf(Panel.Tree))
                        .setRenderer(panelOrder.indexOf(Panel.Renderer))
                        .setProperties(panelOrder.indexOf(Panel.Properties))
                        .build()
                )
                .build()
        }
    }
}

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings
        get() = Settings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Settings {
        try {
            return Settings.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) {
        t.writeTo(output)
    }

}

val Context.settingsStore: DataStore<Settings> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer,
)
