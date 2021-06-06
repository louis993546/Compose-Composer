package com.louis993546.composecomposer.di

import android.content.Context
import com.louis993546.composecomposer.App
import com.louis993546.composecomposer.MainActivity
import com.louis993546.composecomposer.ui.editor.EditorScreenDependencies
import com.louis993546.composecomposer.ui.finder.FinderScreenDependencies

interface Injector {
    fun inject(app: App)

    fun inject(into: EditorScreenDependencies, context: Context)

    fun inject(into: FinderScreenDependencies, context: Context)
}
