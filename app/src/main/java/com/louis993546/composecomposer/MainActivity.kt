package com.louis993546.composecomposer

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
import kotlin.time.measureTimedValue
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : ComponentActivity() {
  lateinit var pageRepository: PageRepository
  lateinit var settingsRepository: SettingsRepository

  @ExperimentalStdlibApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injector.inject(this)

    setContent {
      val panelOrders = settingsRepository.getPanelOrderFlow().collectAsState(initial = emptyList())
      var page by remember { mutableStateOf(defaultPage) } // TODO swap out defaultPage

      val coroutineScope = rememberCoroutineScope()
      val onSaveClick: () -> Unit = { coroutineScope.launch { pageRepository.savePage(page) } }
      val onReadListClick: () -> Unit = {
        coroutineScope.launch {
          pageRepository.getPageInfoList().forEach { Timber.d(it.toString()) }
        }
      }
      val onReadPageClick: () -> Unit = {
        coroutineScope.launch {
          pageRepository.getPageInfoList().firstOrNull()?.run {
            Timber.d(pageRepository.getPage(this)?.toString() ?: "empty")
          }
        }
      }

      val onShufflePanelOrderClicked: () -> Unit = {
        coroutineScope.launch { settingsRepository.savePanelOrder(panelOrders.value.shuffled()) }
      }

      ComposeComposerTheme {
        Scaffold(
            topBar = {
              TopBar(
                  onSaveClicked = onSaveClick,
                  onReadListClicked = onReadListClick,
                  onReadPageClicked = onReadPageClick,
                  onShufflePanelOrderClicked = onShufflePanelOrderClicked,
              )
            },
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

  /**
   * (Hopefully) Slightly more efficient way to recursively way to replace a single node, by
   * skipping the rest once a single replacement has been done
   *
   * Commented out because it is often slower (benchmark via [measureTimedValue])
   */
  //  @OptIn(ExperimentalTime::class)
  //  private fun recursivelyCopyWithNewNode(children: List<Node>, newNode: Node): List<Node> {
  //    var currentIndex = 0
  //    var replacementHasBeenDone = false
  //    val newChildren = mutableListOf<Node>()
  //    while (currentIndex < children.size && !replacementHasBeenDone) {
  //      val currentNode = children[currentIndex]
  //      val updatedNode = currentNode.copyWithNewNode(newNode)
  //      if (currentNode == updatedNode) replacementHasBeenDone = true
  //      newChildren.add(updatedNode)
  //      currentIndex += 1
  //    }
  //    newChildren.addAll(children.takeLast(children.size - currentIndex))
  //    return newChildren
  //  }
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

private fun Configuration.isPhoneSize(): Boolean = screenWidthDp <= 411

private fun Configuration.isPhabletSize(): Boolean = screenWidthDp <= 800

private fun Configuration.isTabletSize(): Boolean = screenWidthDp > 800

@Composable
fun Page(
    modifier: Modifier = Modifier,
    page: Page,
    onNodeSelected: (Node) -> Unit,
) {
  Tree(
      modifier = modifier,
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
    onSaveClicked: () -> Unit,
    onReadListClicked: () -> Unit,
    onReadPageClicked: () -> Unit,
    onShufflePanelOrderClicked: () -> Unit,
) {
  TopAppBar(modifier = modifier) {
    Text("Composer")
    Button(onClick = onSaveClicked) { Text("Serialize to logcat") }
    Button(onClick = onReadListClicked) { Text("Read list of files") }
    Button(onClick = onReadPageClicked) { Text("Read the first page") }
    Button(onClick = onShufflePanelOrderClicked) { Text("shuffle panel order") }
  }
}

enum class Panel {
  Tree,
  Renderer,
  Properties
}
