package com.louis993546.composecomposer.di

import android.content.Context
import com.louis993546.composecomposer.App
import com.louis993546.composecomposer.BuildConfig
import com.louis993546.composecomposer.Database
import com.louis993546.composecomposer.data.FilePageRepository
import com.louis993546.composecomposer.data.adapter.ColorMoshiAdapter
import com.louis993546.composecomposer.data.adapter.DpMoshiAdapter
import com.louis993546.composecomposer.data.adapter.InstantMoshiAdapter
import com.louis993546.composecomposer.data.settings.DataStoreSettingsRepository
import com.louis993546.composecomposer.data.settings.settingsStore
import com.louis993546.composecomposer.ui.editor.EditorScreenDependencies
import com.louis993546.composecomposer.ui.finder.FinderScreenDependencies
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

object ManualInjector : Injector {
    @OptIn(ExperimentalStdlibApi::class)
    private val moshi by lazy {
        Moshi.Builder()
            .addAdapter(DpMoshiAdapter())
            .addAdapter(ColorMoshiAdapter())
            .addAdapter(InstantMoshiAdapter())
            .build()
    }

    private lateinit var driver: SqlDriver
    private val database by lazy { Database(driver) }

    override fun inject(app: App) {
        driver = AndroidSqliteDriver(Database.Schema, app, "compose_composer.db")

        app.timberTree = if (BuildConfig.DEBUG) Timber.DebugTree() else null
    }

    override fun inject(into: EditorScreenDependencies, context: Context) {
        into.pageRepository = FilePageRepository(
            moshi = moshi,
            database = database,
            context = context,
            dispatcher = Dispatchers.IO
        )

        into.settingsRepository = DataStoreSettingsRepository(
            settingsStore = context.settingsStore
        )
    }

    override fun inject(into: FinderScreenDependencies, context: Context) {
        into.pageRepository = FilePageRepository(
            moshi = moshi,
            database = database,
            context = context,
            dispatcher = Dispatchers.IO
        )
    }
}
