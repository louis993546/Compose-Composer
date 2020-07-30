package com.louis993546.composecomposer.sidebar

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
import androidx.ui.layout.preferredWidth
import androidx.ui.material.Button
import androidx.ui.unit.dp
import com.louis993546.composecomposer.model.Design


@Composable
fun SceneSetter(design: Design, setDesign: (Design) -> Unit) {
    val (openDialog, setOpenDialog) = state { false }
    Row(horizontalArrangement = Arrangement.Center) {
        Button(onClick = { setOpenDialog(true) }) {
            Text(text = "Width: ${design.width}")
        }
        Spacer(Modifier.preferredWidth(24.dp))
        Button(onClick = {}) {
            Text(text = "Height: ${design.height}")
        }

        if (openDialog) {
            SizeInputDialog(
                    originalValue = design.width,
                    onNewValue = {
                        setDesign(design.copy(width = it))
                        setOpenDialog(false)
                    },
                    onCancel = { setOpenDialog(false) }
            )
        }
    }
}