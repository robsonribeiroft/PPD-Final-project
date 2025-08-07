package com.robsonribeiro.component.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import com.robsonribeiro.component.BentoComponent
import com.robsonribeiro.model.ConfirmationDialogInfo
import com.robsonribeiro.component.window.WindowBarComponent
import com.robsonribeiro.helper.screenDimensionsWithBounds
import com.robsonribeiro.values.BackgroundGradient
import com.robsonribeiro.values.BlackRich
import com.robsonribeiro.values.Padding
import com.robsonribeiro.values.RedPantoneDarker
import com.robsonribeiro.values.alpha
import kommradiuslocationppd.composeapp.generated.resources.Res
import kommradiuslocationppd.composeapp.generated.resources.dialog_confirmation_cancel
import kommradiuslocationppd.composeapp.generated.resources.dialog_confirmation_ok
import org.jetbrains.compose.resources.stringResource

private const val DIALOG_WINDOW_BAR = "Confirm Action"

@Composable
fun ConfirmationDialog(
    content: ConfirmationDialogInfo,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {

    DialogWindow(
        onCloseRequest = onDismissRequest,
        undecorated = true,
        transparent = true,
        state = DialogState(
            size = screenDimensionsWithBounds(
                verticalWeight = 0.3f,
                horizontalWeight = 0.4f
            )
        ),
        onPreviewKeyEvent = { keyEvent ->
            when (keyEvent.key) {
                Key.Escape -> {
                    content.onDismiss
                    true
                }
                Key.Enter, Key.NumPadEnter -> {
                    content.onDismiss
                    true
                }
                else -> false
            }
        }
    ) {
        BentoComponent(
            modifier
            .fillMaxSize()
            .border(
                width = Padding.single,
                color = Color.Gray,
                shape = RoundedCornerShape(Padding.regular)
            ),
            backgroundColor = Color.White.alpha(0.8f)
        ) {
            Column {
                WindowDraggableArea {
                    WindowBarComponent(
                        title = DIALOG_WINDOW_BAR,
                        barGradientColors = Color.BackgroundGradient
                    ) { onDismissRequest() }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterHorizontally)
                        .padding(Padding.large),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier,
                        text = content.title,
                        style = MaterialTheme.typography.h6.copy(color = Color.BlackRich),
                    )

                    Text(
                        modifier = Modifier,
                        text = content.description,
                        color = Color.BlackRich,
                        style = MaterialTheme.typography.body1,
                    )
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(Padding.large),
                    horizontalArrangement = Arrangement.spacedBy(Padding.regular, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.BlackRich
                        ),
                        border = BorderStroke(
                            Padding.single,
                            Color.BlackRich
                        ),
                        onClick = {
                            onDismissRequest()
                            content.onDismiss()
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.dialog_confirmation_cancel)
                        )
                    }
                    Button(
                        onClick = {
                            onDismissRequest()
                            content.onConfirm()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.RedPantoneDarker,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(Res.string.dialog_confirmation_ok),
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            }
        }
    }
}