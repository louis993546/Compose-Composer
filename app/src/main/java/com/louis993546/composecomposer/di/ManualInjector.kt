package com.louis993546.composecomposer.di

import com.louis993546.composecomposer.App
import com.louis993546.composecomposer.BuildConfig
import com.louis993546.composecomposer.Database
import com.louis993546.composecomposer.MainActivity
import com.louis993546.composecomposer.data.FilePageRepository
import com.louis993546.composecomposer.data.adapter.ColorMoshiAdapter
import com.louis993546.composecomposer.data.adapter.DpMoshiAdapter
import com.louis993546.composecomposer.data.settings.DataStoreSettingsRepository
import com.louis993546.composecomposer.data.settings.SettingsRepository
import com.louis993546.composecomposer.data.settings.settingsStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

@ExperimentalStdlibApi
object ManualInjector : Injector {
    private val moshi by lazy {
        Moshi.Builder()
            .addAdapter(DpMoshiAdapter())
            .addAdapter(ColorMoshiAdapter())
            .build()
    }

    private lateinit var driver: SqlDriver
    private val database by lazy { Database(driver) }

    override fun inject(app: App) {
        driver = AndroidSqliteDriver(Database.Schema, app, "compose_composer.db")

        app.timberTree = if (BuildConfig.DEBUG) Timber.DebugTree() else null
    }

    override fun inject(mainActivity: MainActivity) {
        with(mainActivity) {
            pageRepository = FilePageRepository(
                moshi = moshi,
                database = database,
                context = mainActivity,
                dispatcher = Dispatchers.IO
            )

            settingsRepository = DataStoreSettingsRepository(
                settingsStore = settingsStore
            )
        }
    }
}
