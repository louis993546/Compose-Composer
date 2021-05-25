package com.louis993546.composecomposer

import android.app.Application
import androidx.activity.ComponentActivity
import com.louis993546.composecomposer.di.Injector
import com.louis993546.composecomposer.di.ManualInjector
import timber.log.Timber

@ExperimentalStdlibApi
@Suppress("unused")
class App : Application() {
    val injector: Injector = ManualInjector

    var timberTree: Timber.Tree? = null

    override fun onCreate() {
        super.onCreate()
        injector.inject(this)

        timberTree?.run { Timber.plant(this) }
    }
}

@ExperimentalStdlibApi
val ComponentActivity.injector: Injector
    get() = (applicationContext as App).injector
