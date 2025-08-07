package com.robsonribeiro.component.dialog

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import com.robsonribeiro.component.BentoComponent
import com.robsonribeiro.component.window.WindowBarComponent
import com.robsonribeiro.helper.screenDimensionsWithBounds
import com.robsonribeiro.values.BackgroundGradient
import com.robsonribeiro.values.BlackRich
import com.robsonribeiro.values.GreenEmerald
import com.robsonribeiro.values.Padding
import com.robsonribeiro.values.empty


private const val BUTTON_OK = "Ok"
private const val DIALOG_WINDOW_BAR = "Warning"


@Composable
fun InfoDialog(
    content: Pair<String, String>? = null,
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
                    onDismissRequest()
                    true
                }
                Key.Enter, Key.NumPadEnter -> {
                    onDismissRequest()
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
            )
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
                        text = content?.first ?: String.empty,
                        style = MaterialTheme.typography.h6.copy(color = Color.BlackRich),
                    )

                    Text(
                        modifier = Modifier,
                        text = content?.second ?: String.empty,
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
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.GreenEmerald,
                            contentColor = Color.White
                        )
                    ) {
                        Text(BUTTON_OK)
                    }
                }
            }
        }
    }
}