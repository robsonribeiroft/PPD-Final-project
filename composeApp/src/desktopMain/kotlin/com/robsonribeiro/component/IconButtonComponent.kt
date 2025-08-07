package com.robsonribeiro.component

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.robsonribeiro.values.BlackRich
import com.robsonribeiro.values.empty
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun IconButtonComponent(
    iconResource: DrawableResource,
    iconColor: Color = Color.BlackRich,
    buttonColor: Color = Color.LightGray,
    modifier: Modifier = Modifier,
    onClick: ()->Unit
) {
    ButtonComponent(
        modifier = modifier,
        color = buttonColor,
        onClick = onClick
    ) {
        Icon(
            vectorResource(iconResource),
            String.empty,
            modifier,
            iconColor
        )
    }
}