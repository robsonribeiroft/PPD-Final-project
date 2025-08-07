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
import com.robsonribeiro.model.ConnectionData
import com.robsonribeiro.ppd_project_broker.helper.NetworkInputValidator
import com.robsonribeiro.values.*

private const val TEXT_FIELD_HINT_CLIENT_ID_HINT = "e.g., your name, 1_bit_2_win"
private const val TEXT_FIELD_BROKER_ADDRESS_HINT = "Default Address on ${ConnectionData.DefaultValues.BROKER_HOST_ADDRESS}:${ConnectionData.DefaultValues.BROKER_PORT}"
private const val TEXT_FIELD_RMI_ADDRESS_HINT = "Default Address on ${ConnectionData.DefaultValues.RMI_HOST_ADDRESS}:${ConnectionData.DefaultValues.RMI_PORT}"
private const val TEXT_FIELD_NICKNAME_TITLE = "Enter Your Nickname"
private const val TEXT_BROKER_ADDRESS_TITLE = "Enter Broker Server Address"
private const val TEXT_RMI_ADDRESS_TITLE = "Enter RMI Server Address"
const val BUTTON_JOIN = "Join Server"

@Composable
fun ConnectionDialog(
    modifier: Modifier = Modifier,
    defaultRmiValue: String = "${ConnectionData.DefaultValues.RMI_HOST_ADDRESS}:${ConnectionData.DefaultValues.RMI_PORT}",
    defaultBrokerValue: String = "${ConnectionData.DefaultValues.BROKER_HOST_ADDRESS}:${ConnectionData.DefaultValues.BROKER_PORT}",
    onConnect: (String, String, Int, String,Int) -> Unit,
    onDismissRequest: () -> Unit
) {

    var textFieldClientIdHasFocus by remember { mutableStateOf(false) }
    var textFieldClientIdValue by remember { mutableStateOf(String.empty) }
    var validationClientIdResult by remember { mutableStateOf(NetworkInputValidator.validateClientId(String.empty)) }
    val textFieldClientIdHintText = {
        if (textFieldClientIdHasFocus || textFieldClientIdValue.isNotEmpty())
            String.empty
        else
            TEXT_FIELD_HINT_CLIENT_ID_HINT
    }

    var textFieldBrokerAddressHasFocus by remember { mutableStateOf(false) }
    var textFieldBrokerValueAddress by remember { mutableStateOf(String.empty) }
    var validationBrokerAddressResult by remember { mutableStateOf(NetworkInputValidator.validateServerAddress(String.empty)) }
    val textFieldBrokerAddressHintText = {
        if (textFieldBrokerAddressHasFocus || textFieldBrokerValueAddress.isNotEmpty())
            String.empty
        else
            TEXT_FIELD_BROKER_ADDRESS_HINT
    }

    var textFieldRmiAddressHasFocus by remember { mutableStateOf(false) }
    var textFieldRmiValueAddress by remember { mutableStateOf(String.empty) }
    var validationRmiAddressResult by remember { mutableStateOf(NetworkInputValidator.validateServerAddress(String.empty)) }
    val textFieldRmiAddressHintText = {
        if (textFieldRmiAddressHasFocus || textFieldRmiValueAddress.isNotEmpty())
            String.empty
        else
            TEXT_FIELD_RMI_ADDRESS_HINT
    }

    val attemptConnection = {
        val brokerAddress = textFieldBrokerValueAddress.ifEmpty { defaultBrokerValue }
        val rmiAddress = textFieldRmiValueAddress.ifEmpty { defaultRmiValue }
        val inputClientId = textFieldClientIdValue
        val brokerAddressValidatorResult = NetworkInputValidator.validateServerAddress(brokerAddress)
        val rmiAddressValidatorResult = NetworkInputValidator.validateServerAddress(rmiAddress)
        val clientIdValidatorResult = NetworkInputValidator.validateClientId(inputClientId)
        validationBrokerAddressResult = brokerAddressValidatorResult
        validationRmiAddressResult = rmiAddressValidatorResult
        if (validationBrokerAddressResult.isValid && validationRmiAddressResult.isValid && clientIdValidatorResult.isValid) {
            onConnect(
                textFieldClientIdValue,
                brokerAddressValidatorResult.host!!,
                brokerAddressValidatorResult.port!!,
                rmiAddressValidatorResult.host!!,
                rmiAddressValidatorResult.port!!
            )
        }
    }

    DialogWindow(
        onCloseRequest = onDismissRequest,
        undecorated = true,
        transparent = true,
        state = DialogState(
            size = screenDimensionsWithBounds(
                verticalWeight = 0.7f,
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
            backgroundColor = Color.BaseBackground.alpha(0.9f)
        ) {
            Column {
                WindowDraggableArea {
                    WindowBarComponent(
                        title = "Connect to Server",
                    ) { onDismissRequest() }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(Padding.large),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = TEXT_FIELD_NICKNAME_TITLE,
                        style = MaterialTheme.typography.h6.copy(color = Color.BlackRich),
                    )

                    OutlinedTextField(
                        value = textFieldClientIdValue,
                        onValueChange = {
                            textFieldClientIdValue = it
                            validationClientIdResult = NetworkInputValidator.validateClientId(it)
                        },
                        label = { Text(textFieldClientIdHintText()) },
                        isError = !validationClientIdResult.isValid && textFieldClientIdValue.isNotEmpty(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = VisualTransformation.None,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                textFieldClientIdHasFocus = focusState.hasFocus
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

                    if (!validationClientIdResult.isValid) {
                        Text(
                            text = validationClientIdResult.errorMessage.orEmpty(),
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(top = Padding.small)
                                .align(Alignment.Start)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(Padding.large),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = TEXT_BROKER_ADDRESS_TITLE,
                        style = MaterialTheme.typography.h6.copy(color = Color.BlackRich),
                    )

                    OutlinedTextField(
                        value = textFieldBrokerValueAddress,
                        onValueChange = {
                            textFieldBrokerValueAddress = it
                            validationBrokerAddressResult = NetworkInputValidator.validateServerAddress(it)
                        },
                        label = { Text(textFieldBrokerAddressHintText()) },
                        isError = !validationBrokerAddressResult.isValid && textFieldBrokerValueAddress.isNotEmpty(), // Show error only if invalid and not empty
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = VisualTransformation.None,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                textFieldBrokerAddressHasFocus = focusState.hasFocus
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

                    if (!validationBrokerAddressResult.isValid && textFieldBrokerValueAddress.isNotEmpty()) {
                        Text(
                            text = validationBrokerAddressResult.errorMessage ?: String.empty,
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(top = Padding.small)
                                .align(Alignment.Start)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(Padding.large),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = TEXT_RMI_ADDRESS_TITLE,
                        style = MaterialTheme.typography.h6.copy(color = Color.BlackRich),
                    )

                    OutlinedTextField(
                        value = textFieldRmiValueAddress,
                        onValueChange = {
                            textFieldRmiValueAddress = it
                            validationRmiAddressResult = NetworkInputValidator.validateServerAddress(it)
                        },
                        label = { Text(textFieldRmiAddressHintText()) },
                        isError = !validationRmiAddressResult.isValid && textFieldRmiValueAddress.isNotEmpty(), // Show error only if invalid and not empty
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = VisualTransformation.None,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                textFieldRmiAddressHasFocus = focusState.hasFocus
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

                    if (!validationRmiAddressResult.isValid && textFieldRmiValueAddress.isNotEmpty()) {
                        Text(
                            text = validationRmiAddressResult.errorMessage ?: String.empty,
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
                        enabled = validationClientIdResult.isValid && (validationBrokerAddressResult.isValid || textFieldBrokerValueAddress.isEmpty()),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.GreenEmerald,
                            contentColor = Color.White
                        )
                    ) {
                        Text(BUTTON_JOIN)
                    }
                }
            }
        }
    }
}
