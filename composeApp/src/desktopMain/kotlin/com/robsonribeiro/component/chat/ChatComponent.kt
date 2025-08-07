package com.robsonribeiro.component.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.robsonribeiro.component.BentoComponent
import com.robsonribeiro.model.ChatMessage
import com.robsonribeiro.values.BaseBackground
import com.robsonribeiro.values.BlackRich
import com.robsonribeiro.values.Padding
import com.robsonribeiro.values.TextSize
import com.robsonribeiro.values.alpha
import com.robsonribeiro.values.empty

const val CHAT_LIST_HEADER = "CHAT"

@Composable
fun ChatComponent(
    modifier: Modifier,
    chatIsEnabled: Boolean,
    currentChat: String? = CHAT_LIST_HEADER,
    messages: List<ChatMessage>,
    onBackClick: ()-> Unit,
    sendMessage: (String)->Unit
) {

    BentoComponent(
        modifier = Modifier
            .fillMaxSize()
            .padding(Padding.large)
            .background(color = Color.Transparent)
    ) {
        Column (
            modifier = modifier
                .fillMaxSize()
        ) {
            ChatHeader(Modifier.fillMaxWidth(), currentChat ?: CHAT_LIST_HEADER, onBackClick)
            ChatListComponent(
                modifier = Modifier.weight(1f),
                messages = messages
            )
            ChatTextFieldComponent(
                modifier = Modifier.fillMaxWidth(),
                onSend = sendMessage,
                isEnabled = chatIsEnabled
            )
        }
    }
}

@Composable
fun ChatHeader(
    modifier: Modifier,
    currentChat: String,
    onBackClick: ()-> Unit
) {
    Column(
        modifier = modifier
            .background(color = Color.BlackRich.alpha(0.5f))
            .padding(top = Padding.regular),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Padding.regular)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Padding.regular, Alignment.CenterHorizontally)
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(Padding.small),
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = String.empty,
                    tint = Color.White
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = currentChat,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    lineHeight = TextSize.large,
                    fontSize = TextSize.large,
                    color = Color.White
                )
            )
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.BaseBackground,
            thickness = Padding.single
        )
    }
}