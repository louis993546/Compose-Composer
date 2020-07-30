package com.louis993546.composecomposer.sidebar

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.Dp
import androidx.ui.unit.dp

@Composable
fun SizeInputDialog(
        originalValue: Dp,
        onNewValue: (Dp) -> Unit,
        onCancel: () -> Unit
) {
    Dialog(onCloseRequest = {}) {
        val (value, setValue) = state { originalValue }
        Column(
                modifier = Modifier
                        .drawBackground(color = MaterialTheme.colors.surface)
                        .fillMaxWidth()
                        .padding(16.dp)
        ) {
            TextField(
                    keyboardType = KeyboardType.Number,
                    value = TextFieldValue(value.value.toString()),
                    onValueChange = { setValue(it.text.toFloat().dp) }
            )
            Row {
                Button(onClick = { onCancel() }) { Text(text = "Cancel") }
                Spacer(modifier = Modifier.preferredWidth(4.dp))
                Button(onClick = { onNewValue(value) }) { Text(text = "Save") }
            }
        }
    }
}