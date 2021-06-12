package com.louis993546.composecomposer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.louis993546.composecomposer.ui.editor.EditorScreen
import com.louis993546.composecomposer.ui.editor.theme.ComposeComposerTheme
import com.louis993546.composecomposer.ui.finder.FinderScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }

            val navController = rememberNavController()
            ComposeComposerTheme {
                ProvideWindowInsets {
                    NavHost(
                        modifier = Modifier.systemBarsPadding(),
                        navController = navController,
                        startDestination = Screen.Finder.name
                    ) {
                        composable(Screen.Editor.name) {
                            EditorScreen(
                                navController = navController,
                                injector = injector,
                            )
                        }
                        composable(Screen.Finder.name) {
                            FinderScreen(
                                navController = navController,
                                injector =  injector,
                            )
                        }
                    }
                }

            }
        }
    }
}

enum class Screen {
    Finder,
    Editor
}

/**
 * TODO write a lint that forces me to use this navigate, instead of the string/resId navigate
 */
fun NavController.navigate(screen: Screen) {
    this.navigate(screen.name)
}