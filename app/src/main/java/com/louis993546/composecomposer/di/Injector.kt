package com.louis993546.composecomposer.di

import com.louis993546.composecomposer.App
import com.louis993546.composecomposer.MainActivity

interface Injector {
    fun inject(app: App)

    fun inject(mainActivity: MainActivity)
}
