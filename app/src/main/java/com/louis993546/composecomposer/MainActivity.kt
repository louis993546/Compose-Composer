package com.louis993546.composecomposer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
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
    data class Text(val text: String): Component() {
        override fun meaningfulName() = "Text (${text})"
    }

    data class Row(val children: List<Component>): Component(hasChildren = true) {
        override fun meaningfulName() = "Row (${children.size})"
    }
    data class Column(val children: List<Component>): Component(hasChildren = true) {
        override fun meaningfulName() = "Column (${children.size})"
    }

    data class Button(val text: String): Component() {
        override fun meaningfulName() = "Button ($text)"
    }

    abstract fun meaningfulName(): String
}

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

@Composable
fun ComponentToolbar() {
    Row {
        Text(text = "Row")
        Text(text = "Column")
        Text(text = "Text")
        Text(text = "Text Button")
    }
}

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

@Composable
private fun ComponentTree(modifier: Modifier = Modifier, layer: Int, component: Component) {
    Column(modifier = modifier) {
        ComponentChip(
                modifier = Modifier.padding(start = (layer * 16).dp),
                name = component.meaningfulName()
        )
        // TODO find a better way to do this
        if (component.hasChildren) {
            val newLayer = layer + 1
            (component as? Component.Row)?.children?.forEach {
                ComponentTree(layer = newLayer, component = it)
            }
            (component as? Component.Column)?.children?.forEach {
                ComponentTree(layer = newLayer, component = it)
            }
        }
    }
}

// TODO learn drag and drop
@Composable
fun ComponentChip(modifier: Modifier = Modifier, name: String) {
    Box(
            modifier = modifier.padding(4.dp).fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            backgroundColor = MaterialTheme.colors.secondary
    ) {
        Text(
                modifier = Modifier.padding(4.dp),
                text = name,
                color = MaterialTheme.colors.onSecondary
        )
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