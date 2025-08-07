package com.robsonribeiro.component.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.robsonribeiro.values.*
import kommradiuslocationppd.composeapp.generated.resources.Res
import kommradiuslocationppd.composeapp.generated.resources.ic_send
import org.jetbrains.compose.resources.vectorResource

const val CHAT_TEXT_FIELD_HINT = "Write a message"

@Composable
fun ChatTextFieldComponent(
    placeholderText: String = CHAT_TEXT_FIELD_HINT,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
    onSend: (String) -> Unit
) {
    var text by remember { mutableStateOf(String.empty) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        enabled = isEnabled,
        value = text,
        onValueChange = { text = it },
        placeholder = { Text(placeholderText, color = Color.BaseBackground.alpha(0.5f)) },
        trailingIcon = {
            Icon(
                vectorResource(Res.drawable.ic_send),
                String.empty,
                modifier = Modifier
                    .clickable {
                        if (text.isNotBlank()) {
                            onSend(text.trim())
                            text = String.empty
                        }
                    }
                    .padding(Padding.regular)
            )
        },
        modifier = modifier
            .padding(Padding.regular)
            .focusRequester(focusRequester)
            .onKeyEvent { event ->
                when (event.key) {
                    Key.Escape -> {
                        focusManager.clearFocus()
                        true
                    }

                    Key.Enter, Key.NumPadEnter -> {
                        if (text.isNotBlank()) {
                            onSend(text.trim())
                            text = String.empty
                        }
                        true
                    }

                    Key.Tab -> {
                        focusRequester.requestFocus()
                        true
                    }

                    else -> {
                        false
                    }
                }
            },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        shape = CircleShape,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.BlackRich.alpha(0.5f),
            focusedBorderColor = Color.White,
            trailingIconColor = Color.White,
            disabledTrailingIconColor = Color.White.alpha(0.5f),
            unfocusedBorderColor = Color.White.alpha(0.5f),
            textColor = Color.White
        )
    )
}