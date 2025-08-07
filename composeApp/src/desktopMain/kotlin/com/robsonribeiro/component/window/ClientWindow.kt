package com.robsonribeiro.component.window

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.*
import com.robsonribeiro.component.dialog.ConfirmationDialog
import com.robsonribeiro.model.ConfirmationDialogInfo
import com.robsonribeiro.helper.screenDimensionsWithBounds
import com.robsonribeiro.values.StringResources
import com.robsonribeiro.values.empty
import com.robsonribeiro.viewmodel.MainViewModel
import kommradiuslocationppd.composeapp.generated.resources.Res
import kommradiuslocationppd.composeapp.generated.resources.background_noise
import kommradiuslocationppd.composeapp.generated.resources.ic_send
import org.jetbrains.compose.resources.painterResource

@Composable
fun ClientWindow(
    exitApplication: () -> Unit,
    windowContent: @Composable ColumnScope.()-> Unit
) {
    var windowState by remember {
        mutableStateOf(
            WindowState(
                position = WindowPosition(Alignment.TopCenter),
                placement = WindowPlacement.Floating,
                size = screenDimensionsWithBounds(verticalWeight = 1f, horizontalWeight = 0.5f)
            )
        )
    }

    var showConfirmationDialog by remember { mutableStateOf<ConfirmationDialogInfo?>(null) }

    Window(
        onCloseRequest = {
            showConfirmationDialog = ConfirmationDialogInfo(
                title = "Quit Application",
                description = "Close this windows will make your opponent win\nAre you sure to quit?"
            ) {
                exitApplication()
            }
        },
        title = StringResources.APPLICATION_NAME,
        icon = painterResource(Res.drawable.ic_send),
        resizable = true,
        undecorated = true,
        state = windowState,
        onPreviewKeyEvent = { keyEvent ->
            when {
                keyEvent.isCtrlPressed && keyEvent.key == Key.A -> {
                    windowState = WindowState(
                        position = WindowPosition(Alignment.TopStart),
                        placement = WindowPlacement.Floating,
                        size = screenDimensionsWithBounds(horizontalWeight = 0.5f)
                    )
                    return@Window true
                }
                keyEvent.isCtrlPressed && keyEvent.key == Key.D -> {
                    windowState = WindowState(
                        position = WindowPosition(Alignment.TopEnd),
                        placement = WindowPlacement.Floating,
                        size = screenDimensionsWithBounds(horizontalWeight = 0.5f)
                    )
                    return@Window true
                }
                keyEvent.isCtrlPressed && keyEvent.key == Key.F -> {
                    windowState = WindowState(
                        position = WindowPosition(Alignment.TopCenter),
                        placement = WindowPlacement.Maximized,
                        size = screenDimensionsWithBounds()
                    )
                    return@Window true
                }
                else -> false
            }
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.background_noise),
                contentDescription = String.empty,
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                windowTitleBar {
                    showConfirmationDialog = ConfirmationDialogInfo(
                        title = "Quit Application",
                        description = "Close this windows will make your opponent win\nAre you sure to quit?"
                    ) {
                        exitApplication()
                    }
                }
                windowContent()
            }

            showConfirmationDialog?.let {
                ConfirmationDialog(
                    content = showConfirmationDialog!!
                ) {
                    showConfirmationDialog = null
                }
            }
        }
    }
}

@Composable
private fun WindowScope.windowTitleBar(onExitApplication: ()-> Unit) = WindowDraggableArea {
    WindowBarComponent { onExitApplication() }
}

