package com.louis993546.composecomposer.ui.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import app.cash.exhaustive.Exhaustive
import com.louis993546.composecomposer.R
import com.louis993546.composecomposer.Screen
import com.louis993546.composecomposer.data.PageRepository
import com.louis993546.composecomposer.data.defaultPage
import com.louis993546.composecomposer.data.model.Node
import com.louis993546.composecomposer.data.model.Page
import com.louis993546.composecomposer.data.settings.SettingsRepository
import com.louis993546.composecomposer.di.Injector
import com.louis993546.composecomposer.navigate
import com.louis993546.composecomposer.ui.components.VerticalDivider
import com.louis993546.composecomposer.ui.editor.properties.Properties
import com.louis993546.composecomposer.ui.editor.renderer.PageRenderer
import com.louis993546.composecomposer.ui.editor.tree.Tree
import com.louis993546.composecomposer.util.isPhabletSize
import com.louis993546.composecomposer.util.isPhoneSize
import com.louis993546.composecomposer.util.isTabletSize
import kotlinx.coroutines.launch

@Composable
fun EditorScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    injector: Injector,
) {
    val dependencies =  EditorScreenDependencies().apply {
        injector.inject(this, LocalContext.current)
    }
    val settingsRepository = dependencies.settingsRepository
    val pageRepository = dependencies.pageRepository

    val panelOrders by
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

                    navController.navigate(Screen.Finder)
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colors.background,
        ) {
            Body(
                panels = panelOrders,
                page = page,
                updateNode = { newNode ->
                    page = page.copyWithNewNode(newNode)
                    newNode
                },
            )
        }
    }
}

class EditorScreenDependencies {
    lateinit var pageRepository: PageRepository
    lateinit var settingsRepository: SettingsRepository
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
            @Exhaustive
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
            }
            if (index < panels.size) VerticalDivider()
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
