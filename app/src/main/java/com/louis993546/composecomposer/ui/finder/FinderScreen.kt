package com.louis993546.composecomposer.ui.finder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.louis993546.composecomposer.R
import com.louis993546.composecomposer.Screen
import com.louis993546.composecomposer.data.PageRepository
import com.louis993546.composecomposer.data.defaultPage
import com.louis993546.composecomposer.data.model.PageInfo
import com.louis993546.composecomposer.di.Injector
import com.louis993546.composecomposer.navigate
import com.louis993546.composecomposer.ui.components.VerticalDivider
import kotlinx.coroutines.launch

@Composable
fun FinderScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    injector: Injector,
) {
    val dependencies = FinderScreenDependencies().apply {
        injector.inject(this, LocalContext.current)
    }

    val pageInfoList by dependencies.pageRepository
        .getPageInfoListFLow()
        .collectAsState(initial = emptyList())

    val coroutineScope = rememberCoroutineScope()
    val onNewFileClick: () -> Unit = {
        coroutineScope.launch { dependencies.pageRepository.savePage(defaultPage) }
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopBar() },
    ) { innerPadding ->
        // TODO for smaller device, make the detail some kind of dismiss-able popup instead
        Row(
            modifier = Modifier.padding(innerPadding),
        ) {
            Scaffold(
                modifier = Modifier.weight(1f),
                floatingActionButton = { NewFileButton(onClick = onNewFileClick) },
            ){
                LazyColumn(modifier = Modifier.padding(it)) {
                    items(pageInfoList, key = { it.id }) { pageInfo ->
                        PageInfoItem(
                            modifier = Modifier.clickable {
                                // TODO pass in the id, and let editor open the file
                                navController.navigate(Screen.Editor)
                            },
                            pageInfo = pageInfo
                        )
                    }
                }
            }
            VerticalDivider()
            Box(
                modifier = Modifier.weight(1f),
            ) {
                Text("TODO master detail")
            }
        }
    }
}

class FinderScreenDependencies {
    lateinit var pageRepository: PageRepository
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text("Compose") },
        modifier = modifier,
//        actions = { },
    )
}

@Composable
fun NewFileButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "New File"
        )
    }
}

/**
 * TODO
 *   It needs to look better
 *   Add created at/last update at meta data
 */
@Composable
fun PageInfoItem(
    modifier: Modifier = Modifier,
    pageInfo: PageInfo,
) {
    Text(
        text = pageInfo.name,
        modifier = modifier.padding(8.dp),
    )
}
