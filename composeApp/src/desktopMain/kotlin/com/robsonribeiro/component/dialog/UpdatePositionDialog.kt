package com.robsonribeiro.component.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import com.robsonribeiro.component.BentoComponent
import com.robsonribeiro.component.window.WindowBarComponent
import com.robsonribeiro.helper.screenDimensionsWithBounds
import com.robsonribeiro.values.*
import kommradiuslocationppd.composeapp.generated.resources.Res
import kommradiuslocationppd.composeapp.generated.resources.dialog_update_position_invalid_input
import kommradiuslocationppd.composeapp.generated.resources.dialog_update_position_ok
import kommradiuslocationppd.composeapp.generated.resources.dialog_update_position_title
import org.jetbrains.compose.resources.stringResource


private const val TEXT_FIELD_HINT = "e.g., 45, 60 or -5.5, 60"
private const val TEXT_FIELD_RADIUS_RANGE_HINT = "A number higher than zero."


@Composable
fun UpdatePositionDialog(
    modifier: Modifier = Modifier,
    currentPosition: String,
    currentRadiusRange: Float,
    onConnect: (Pair<String, Float>) -> Unit,
    onDismissRequest: () -> Unit
) {
    var textFieldHasFocus by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(currentPosition) }
    var validationResult by remember { mutableStateOf(validateInput(currentPosition)) }
    val textFieldHintText = {
        if (textFieldHasFocus || textFieldValue.isNotEmpty())
            String.empty
        else
            TEXT_FIELD_HINT
    }

    var textFieldRadiusRangeHasFocus by remember { mutableStateOf(false) }
    var textFieldRadiusRangeValue by remember { mutableStateOf(currentRadiusRange.toString()) }
    var validationRadiusRangeResult by remember { mutableStateOf(validateRangeInput(currentRadiusRange.toString())) }
    val textFieldRadiusRangeHintText = {
        if (textFieldRadiusRangeHasFocus || textFieldValue.isNotEmpty())
            String.empty
        else
            TEXT_FIELD_RADIUS_RANGE_HINT
    }


    val attemptConnection = {
        val result = validateInput(textFieldValue) && validateRangeInput(textFieldRadiusRangeValue)
        if (result) {
            onConnect(Pair(textFieldValue, textFieldRadiusRangeValue.toFloat()))
        }
    }

    DialogWindow(
        onCloseRequest = onDismissRequest,
        undecorated = true,
        transparent = true,
        state = DialogState(
            size = screenDimensionsWithBounds(
                verticalWeight = 0.4f,
                horizontalWeight = 0.5f
            )
        ),
        onPreviewKeyEvent = { keyEvent ->
            when (keyEvent.key) {
                Key.Escape -> {
                    onDismissRequest()
                    true
                }
                Key.Enter, Key.NumPadEnter -> {
                    attemptConnection()
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
                brush = Brush.linearGradient(
                    colors = listOf(Color.Gray, Color.Transparent),
                ),
                shape = RoundedCornerShape(Padding.regular)
            ),
            backgroundColor = Color.BaseBackground.alpha(0.8f)
        ) {
            Column {
                WindowDraggableArea {
                    WindowBarComponent(
                        title = "Update Position",
                    ) { onDismissRequest() }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(Padding.large),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.dialog_update_position_title),
                        style = MaterialTheme.typography.h6,
                    )

                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = {
                            textFieldValue = it
                            validationResult = validateInput(it)
                        },
                        label = { Text(textFieldRadiusRangeHintText()) },
                        isError = !validationResult && textFieldValue.isNotEmpty(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = VisualTransformation.None,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                textFieldHasFocus = focusState.hasFocus
                            }
                            .onKeyEvent { event ->
                                when (event.key) {
                                    Key.Enter, Key.NumPadEnter -> {
                                        attemptConnection()
                                        true
                                    }
                                    else -> false
                                }
                            },
                        shape = CircleShape,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White,
                            focusedBorderColor = Color.BlueRoyal,
                            unfocusedBorderColor = Color.BaseBackground,
                            errorBorderColor = Color.RedPantoneDarker
                        )
                    )

                    OutlinedTextField(
                        value = textFieldRadiusRangeValue,
                        onValueChange = {
                            textFieldRadiusRangeValue = it
                            validationRadiusRangeResult = validateRangeInput(it)
                        },
                        label = { Text(textFieldHintText()) },
                        isError = !validationRadiusRangeResult && textFieldValue.isNotEmpty(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = VisualTransformation.None,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                textFieldRadiusRangeHasFocus = focusState.hasFocus
                            }
                            .onKeyEvent { event ->
                                when (event.key) {
                                    Key.Enter, Key.NumPadEnter -> {
                                        attemptConnection()
                                        true
                                    }
                                    else -> false
                                }
                            },
                        shape = CircleShape,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White,
                            focusedBorderColor = Color.BlueRoyal,
                            unfocusedBorderColor = Color.BaseBackground,
                            errorBorderColor = Color.RedPantoneDarker
                        )
                    )

                    if (!validationRadiusRangeResult) {
                        Text(
                            text = stringResource(Res.string.dialog_update_position_invalid_input),
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(top = Padding.small)
                                .align(Alignment.Start)
                        )
                    }
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
                        onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = attemptConnection,
                        enabled = validationResult,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.GreenEmerald,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(Res.string.dialog_update_position_ok),
                        )
                    }
                }
            }
        }
    }
}

fun validateRangeInput(input: String): Boolean {
    return try {
        val number = input.toFloat()
        return number > 0
    } catch (_: Exception) {
        false
    }
}


private fun validateInput(input: String = String.empty): Boolean {
    if (input.isEmpty()) return false
    val cleanValue = input.trim().replace(" ", "").split(",")
    try {
        cleanValue.first().toFloat()
        cleanValue.last().toFloat()
    } catch (_: Exception) {
        return false
    }
    return true
}