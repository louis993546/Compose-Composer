package com.louis993546.composecomposer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.louis993546.composecomposer.data.PageRepository
import com.louis993546.composecomposer.data.defaultPage
import com.louis993546.composecomposer.data.model.Node
import com.louis993546.composecomposer.data.model.Page
import com.louis993546.composecomposer.data.settings.SettingsRepository
import com.louis993546.composecomposer.ui.properties.Properties
import com.louis993546.composecomposer.ui.renderer.PageRenderer
import com.louis993546.composecomposer.ui.theme.ComposeComposerTheme
import com.louis993546.composecomposer.ui.tree.Tree
import com.louis993546.composecomposer.util.exhaustive
import com.louis993546.composecomposer.util.isPhabletSize
import com.louis993546.composecomposer.util.isPhoneSize
import com.louis993546.composecomposer.util.isTabletSize
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    lateinit var pageRepository: PageRepository
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        injector.inject(this)

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !MaterialTheme.colors.isLight // don't know why it's flipped ¯\_(ツ)_/¯

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
                        startDestination = "edit"
                    ) {
                        composable("edit") {
                            EditScreen(
                                navController = navController,
                                settingsRepository = settingsRepository,
                                pageRepository = pageRepository,
                            )
                        }
                        composable("test") { Text(text = "Test") }
                    }
                }

            }
        }
    }
}

@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    settingsRepository: SettingsRepository,
    pageRepository: PageRepository,
) {
    val panelOrders =
        settingsRepository.getPanelOrderFlow().collectAsState(initial = emptyList())
    var page by remember { mutableStateOf(defaultPage) } // TODO swap out defaultPage

    val coroutineScope = rememberCoroutineScope()
    val onSaveClick: () -> Unit =
        { coroutineScope.launch { pageRepository.savePage(page) } }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                onSaveClicked = onSaveClick,
                onCloseClicked = {
                    // TODO need to show warning dialog first
                    // navController.popBackStack()

                    navController.navigate("test")
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colors.background,
        ) {
            Body(
                panels = panelOrders.value,
                page = page,
                updateNode = { newNode ->
                    page = page.copyWithNewNode(newNode)
                    newNode
                },
            )
        }
    }
}

private fun Page.copyWithNewNode(newNode: Node): Page =
    this.copy(node = this.node.copyWithNewNode(newNode))

private fun Node.copyWithNewNode(newNode: Node): Node =
    when {
        this.id == newNode.id -> newNode
        this is Node.Column -> copy(children = children.map { it.copyWithNewNode(newNode) })
        this is Node.Row -> copy(children = children.map { it.copyWithNewNode(newNode) })
        else -> this
    }

@Composable
fun Body(
    modifier: Modifier = Modifier,
    panels: List<Panel>,
    page: Page,
    updateNode: (Node) -> Node,
) {
    var currentlySelectedNode by remember { mutableStateOf<Node?>(null) }

    val config = LocalConfiguration.current
    when {
        config.isPhoneSize() -> Text("TODO", modifier = modifier)
        config.isPhabletSize() -> Text("TODO", modifier = modifier)
        config.isTabletSize() ->
            TabletBody(
                modifier = modifier,
                panels = panels,
                page = page,
                currentlySelectedNode = currentlySelectedNode,
                onNodeSelected = { node -> currentlySelectedNode = node },
                onNodeModified = { newNode -> currentlySelectedNode = updateNode(newNode) },
            )
    }
}

@Composable
fun TabletBody(
    modifier: Modifier = Modifier,
    panels: List<Panel>,
    page: Page,
    currentlySelectedNode: Node?,
    onNodeSelected: (Node) -> Unit,
    onNodeModified: (Node) -> Unit,
) {
    Row(modifier = modifier) {
        panels.forEachIndexed { index, panel ->
            when (panel) {
                Panel.Tree ->
                    Page(
                        modifier = Modifier.weight(1f),
                        page = page,
                        onNodeSelected = onNodeSelected,
                    )
                Panel.Renderer ->
                    PageRenderer(
                        modifier = Modifier.weight(1f),
                        page = page,
                    )
                Panel.Properties ->
                    Properties(
                        modifier = Modifier.weight(1f),
                        node = currentlySelectedNode,
                        onNodeModified = onNodeModified,
                    )
            }.exhaustive
            if (index < panels.size) PanelDivider()
        }
    }
}


@Composable
fun Page(
    modifier: Modifier = Modifier,
    page: Page,
    onNodeSelected: (Node) -> Unit,
) {
    Tree(
        modifier = modifier.verticalScroll(rememberScrollState()),
        node = page.node,
        onNodeSelected = onNodeSelected,
    )
}

/** Copy of [androidx.compose.material.Divider] but vertical */
@Composable
fun PanelDivider(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
        modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
    )
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onCloseClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text("Compose") },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onCloseClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_clear_24),
                    contentDescription = "Close"
                )
            }
        },
        actions = {
//            IconButton(onClick = onSaveClicked) { Text("Serialize to logcat") }
//            IconButton(onClick = onReadListClicked) { Text("Read list of files") }
//            IconButton(onClick = onReadPageClicked) { Text("Read the first page") }
//            IconButton(onClick = onShufflePanelOrderClicked) { Text("shuffle panel order") }
            IconButton(onClick = onSaveClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_save_24),
                    contentDescription = "Save",
                )
            }
        },
    )
}

enum class Panel {
    Tree,
    Renderer,
    Properties
}
