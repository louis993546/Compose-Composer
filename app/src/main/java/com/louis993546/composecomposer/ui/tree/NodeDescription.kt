package com.louis993546.composecomposer.ui.tree

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.louis993546.composecomposer.R
import com.louis993546.composecomposer.util.PreviewTheme

@Composable
fun NodeDescription(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    contentDescription: String,
    text: String,
) {
    Row(modifier = modifier) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text)
    }
}

@Preview
@Composable
fun Preview_NodeDescription() {
    PreviewTheme {
        NodeDescription(
            icon = R.drawable.ic_baseline_image_24,
            contentDescription = "",
            text = "NodeDescription"
        )
    }
}