package com.robsonribeiro.component.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import com.robsonribeiro.component.IconButtonComponent
import com.robsonribeiro.values.*
import kommradiuslocationppd.composeapp.generated.resources.Res
import kommradiuslocationppd.composeapp.generated.resources.ic_close
import kommradiuslocationppd.composeapp.generated.resources.ic_minimize
import kommradiuslocationppd.composeapp.generated.resources.ic_send
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WindowBarComponent(
    title: String = StringResources.APPLICATION_NAME,
    titleColor: Color = Color.BlackRich,
    iconColor: Color = Color.White,
    barColor: Color = Color.BlackRich.copy(alpha = 0.5f),
    barGradientColors: List<Color>? = Color.BackgroundGradient,
    modifier: Modifier = Modifier,
    onMinimize: (()-> Unit)? = null,
    onExitWindow: ()-> Unit,
) {

    var isHoverOverMinimize by remember { mutableStateOf(false) }
    var isHoverOverClose by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = if (barGradientColors.isNullOrEmpty()) listOf(barColor, barColor) else barGradientColors
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Padding.regular),
            horizontalArrangement = Arrangement.spacedBy(Padding.regular),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                vectorResource(Res.drawable.ic_send),
                String.empty,
                modifier,
                iconColor
            )
            Text(
                modifier = modifier,
                text = title,
                style = MaterialTheme.typography.h6.copy(color = Color.White)
            )
        }
        Row {
            onMinimize?.let {
                IconButtonComponent(
                    modifier = Modifier
                        .onPointerEvent(eventType = PointerEventType.Enter) { isHoverOverMinimize = true }
                        .onPointerEvent(eventType = PointerEventType.Exit) { isHoverOverMinimize = false },
                    buttonColor = if (isHoverOverMinimize) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                    iconColor = Color.White,
                    iconResource = Res.drawable.ic_minimize
                ) {
                    onMinimize()
                }
            }
            IconButtonComponent(
                modifier = Modifier
                    .onPointerEvent(eventType = PointerEventType.Enter) { isHoverOverClose = true }
                    .onPointerEvent(eventType = PointerEventType.Exit) { isHoverOverClose = false },
                buttonColor = if (isHoverOverClose) Color.RedPantoneDarker else Color.Transparent,
                iconColor = Color.White,
                iconResource = Res.drawable.ic_close,
            ) {
                onExitWindow()
            }
        }
    }
}

