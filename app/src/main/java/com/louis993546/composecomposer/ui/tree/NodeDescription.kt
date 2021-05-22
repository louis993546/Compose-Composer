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
import androidx.compose.ui.unit.dp

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