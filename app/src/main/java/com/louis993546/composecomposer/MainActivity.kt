package com.louis993546.composecomposer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.*
import androidx.ui.graphics.Color
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.AlertDialog
import androidx.ui.material.Button
import androidx.ui.material.TextButton
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.louis993546.composecomposer.ui.ComposeComposerTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val (design, setDesign) = state {
                Design(
                        rootComponent = Component.Column(
                                children = listOf(
                                        Component.Text("add something from the side bar"),
                                        Component.Row(
                                                children = listOf(
                                                        Component.Text("1"),
                                                        Component.Text("2"),
                                                        Component.Text("3"),
                                                        Component.Text("4"),
                                                        Component.Text("5")
                                                )
                                        )
                                )
                        ),
                        width = 360.dp,
                        height = 640.dp,
                        border = Border(2.dp, Color.Black),
                        backgroundColor = Color(0xFFFAFAFA)
                )
            }
            ComposeComposerTheme {
                Row {
                    SideBar(design = design, setDesign = setDesign)
                    Content(
                            modifier = Modifier.size(design.width, design.height)
                                    .drawBackground(design.backgroundColor)
                                    .gravity(Alignment.CenterVertically)
                                    .weight(weight = 1f)
                                    .let {
                                        if (design.border != null) it.drawBorder(design.border)
                                        else it
                                    },
                            component = design.rootComponent
                    )
                }
            }
        }
    }
}

data class Design(
        val rootComponent: Component,
        val width: Dp = 360.dp,
        val height: Dp = 640.dp,
        val border: Border? = null,
        val backgroundColor: Color = Color.White
)

sealed class Component(val hasChildren: Boolean = false) {
    data class Text(val text: String): Component()
    data class Row(val children: List<Component>): Component(hasChildren = true)
    data class Column(val children: List<Component>): Component(hasChildren = true)
    data class Button(val text: String): Component()
}

@Composable
fun SideBar(
        modifier: Modifier = Modifier,
        design: Design,
        setDesign: (Design) -> Unit
) {
    Column(modifier = modifier
            .drawBackground(Color.LightGray)
            .padding(8.dp)
            .fillMaxHeight()
            .preferredWidthIn(maxWidth = 500.dp)
    ) {
        SceneSetter(design, setDesign)
        Render2(layer = 0, columnScope = this, component = design.rootComponent)
    }
}

@Composable
fun SceneSetter(design: Design, setDesign: (Design) -> Unit) {
    val (openDialog, setOpenDialog) = state { false }
    Row {
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
                        .drawBackground(color = Color.White)
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

@Composable
private fun Render2(layer: Int, columnScope: ColumnScope, component: Component) {
    Text(text = "$layer: $component")
    // TODO find a better way to do this
    if (component.hasChildren) {
        val newLayer = layer + 1
        (component as? Component.Row)?.children?.forEach {
            Render2(layer = newLayer, columnScope = columnScope, component = it)
        }
        (component as? Component.Column)?.children?.forEach {
            Render2(layer = newLayer, columnScope = columnScope, component = it)
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier, component: Component) {
    Box(modifier) {
        Render(component = component)
    }
}

@Composable
private fun Render(component: Component) {
    when (component) {
        is Component.Text -> Text(text = component.text)
        is Component.Row -> Row {
            component.children.forEach {
                Render(component = it)
            }
        }
        is Component.Column -> Column {
            component.children.forEach {
                Render(component = it)
            }
        }
        is Component.Button -> Button(onClick = {}) { Render(component = Component.Text(component.text)) }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeComposerTheme {
        Greeting("Android")
    }
}