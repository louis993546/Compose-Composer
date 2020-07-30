package com.louis993546.composecomposer.sidebar

import android.util.Log
import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.drawBackground
import androidx.ui.layout.*
import androidx.ui.material.FloatingActionButton
import androidx.ui.material.MaterialTheme
import androidx.ui.res.vectorResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.louis993546.composecomposer.R
import com.louis993546.composecomposer.model.Design

@Composable
fun SideBar(
        modifier: Modifier = Modifier,
        design: Design,
        setDesign: (Design) -> Unit
) {
    Stack {
        Column(modifier = modifier
                .drawBackground(MaterialTheme.colors.surface)
                .padding(8.dp)
                .fillMaxHeight()
                .preferredWidthIn(maxWidth = 500.dp)
        ) {
            SceneSetter(design, setDesign)
            Spacer(modifier = Modifier.height(16.dp))
            ComponentTree(
                    modifier = Modifier.weight(1f),
                    component = design.rootComponent
            )
        }
        FloatingActionButton(
                modifier = Modifier.gravity(Alignment.BottomEnd).padding(8.dp),
                onClick = { Log.d("qqq", "testing") }
        ) {
            Icon(asset = vectorResource(id = R.drawable.ic_baseline_add_24))
        }
    }
}

@Preview
@Composable
fun Preview_SideBar() {
    SideBar(design = Design.dummy, setDesign = {})
}