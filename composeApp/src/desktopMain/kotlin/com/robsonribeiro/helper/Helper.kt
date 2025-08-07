package com.robsonribeiro.helper

import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import com.robsonribeiro.component.window.WindowBarComponent
import java.awt.GraphicsEnvironment

@Composable
fun WindowScope.windowTitleBar(onExitApplication: ()-> Unit) = WindowDraggableArea {
    WindowBarComponent { onExitApplication() }
}

fun screenDimensionsWithBounds(
    verticalWeight: Float = 1f,
    horizontalWeight: Float = 1f,
    verticalPadding: Int = 40,
    horizontalPadding: Int = 40,
): DpSize {
    val displayWithBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
    if (displayWithBounds == null) {
        val displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode
        val width = (displayMode.width*horizontalWeight).toInt() - horizontalPadding
        val height = (displayMode.height*verticalWeight).toInt() - verticalPadding
        return DpSize(width.dp, height.dp)
    }
    val width = (displayWithBounds.width * horizontalWeight).toInt()
    val height = (displayWithBounds.height * verticalWeight).toInt()
    return DpSize(width.dp, height.dp)
}
