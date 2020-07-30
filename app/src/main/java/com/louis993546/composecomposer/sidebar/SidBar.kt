package com.louis993546.composecomposer.sidebar

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.drawBackground
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxHeight
import androidx.ui.layout.padding
import androidx.ui.layout.preferredWidthIn
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.dp
import com.louis993546.composecomposer.model.Design

@Composable
fun SideBar(
        modifier: Modifier = Modifier,
        design: Design,
        setDesign: (Design) -> Unit
) {
    Column(modifier = modifier
            .drawBackground(MaterialTheme.colors.surface)
            .padding(8.dp)
            .fillMaxHeight()
            .preferredWidthIn(maxWidth = 500.dp)
    ) {
        SceneSetter(design, setDesign)
        ComponentTree(
                modifier = Modifier.weight(1f),
                layer = 0,
                component = design.rootComponent
        )
        ComponentToolbar()
    }
}