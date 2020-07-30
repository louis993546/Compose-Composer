package com.louis993546.composecomposer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.drawBackground
import androidx.ui.foundation.drawBorder
import androidx.ui.layout.Row
import androidx.ui.layout.size
import com.louis993546.composecomposer.model.Design
import com.louis993546.composecomposer.preview.Preview
import com.louis993546.composecomposer.sidebar.SideBar
import com.louis993546.composecomposer.theming.ComposeComposerTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val (design, setDesign) = state {
                Design.dummy
            }
            ComposeComposerTheme {
                Row {
                    SideBar(design = design, setDesign = setDesign)
                    Preview(
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
