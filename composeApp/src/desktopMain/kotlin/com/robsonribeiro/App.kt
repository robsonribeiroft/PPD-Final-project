package com.robsonribeiro

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.robsonribeiro.component.chat.ChatComponent
import com.robsonribeiro.component.connectionComponent.ConnectionComponent
import com.robsonribeiro.component.contact.ContactsComponent
import com.robsonribeiro.component.dialog.ConfirmationDialog
import com.robsonribeiro.component.dialog.ConnectionDialog
import com.robsonribeiro.component.dialog.UpdatePositionDialog
import com.robsonribeiro.model.ConfirmationDialogInfo
import com.robsonribeiro.model.isNotNull
import com.robsonribeiro.model.isNull
import com.robsonribeiro.model.positionDisplay
import com.robsonribeiro.values.Padding
import com.robsonribeiro.values.empty
import com.robsonribeiro.viewmodel.MainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(viewModel: MainViewModel) {

    val myUser by viewModel.myUser.collectAsState()
    val connectionData by viewModel.connectionData.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val currentChatContact by viewModel.currentChatContact.collectAsState()
    val chatMessages by viewModel.currentChatMessages.collectAsState()

    var showConnectionDialog by remember { mutableStateOf(false) }
    var showUpdatePositionDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf<ConfirmationDialogInfo?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Padding.regular, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        ConnectionComponent(
            modifier = Modifier.fillMaxWidth(),
            connectionData = connectionData,
            user = myUser,
            onJoinClick = {
                showConnectionDialog = true
            },
            updatePosition = {
                showUpdatePositionDialog = true
            },
            toggleConnection = { isOnline ->
                viewModel.toggleConnection(isOnline)
            },
            disconnectedClick = {
                showConfirmationDialog = ConfirmationDialogInfo(
                    title = "Disconnect From Server",
                    description = "Do you want disconnect from the server?",
                    onDismiss = {
                        showConfirmationDialog = null
                    },
                    onConfirm = {
                        viewModel.disconnect()
                        showConfirmationDialog = null
                    }
                )
            }
        )

        AnimatedVisibility(
            visible = currentChatContact.isNotNull(),
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            ChatComponent(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                chatIsEnabled = true,
                currentChat = currentChatContact,
                chatMessages,
                onBackClick = {
                    viewModel.closeChatWithContact()
                }
            ) { message ->
                viewModel.sendDirectMessage(message)
            }
        }
        AnimatedVisibility(
            visible = currentChatContact.isNull(),
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            ContactsComponent(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contacts = contacts,
                user = myUser
            ) { contact ->
                viewModel.openChatWithContact(contact)
            }
        }
    }

    if (showConnectionDialog) {
        ConnectionDialog(
            onDismissRequest = {
                showConnectionDialog = false
            },
            onConnect = {id, brokerHost, brokerPort, rmiHost, rmiPort ->
                viewModel.connectServer(
                    id,
                    brokerHost,
                    brokerPort,
                    rmiHost,
                    rmiPort
                )
                showConnectionDialog = false
            }
        )
    }

    if (showUpdatePositionDialog) {
        UpdatePositionDialog(
            currentPosition = myUser?.positionDisplay() ?: String.empty,
            currentRadiusRange = myUser?.distanceRadius ?: 0f,
            onDismissRequest = {
                showUpdatePositionDialog = false
            },
            onConnect = { newPositionData ->
                viewModel.updatePosition(newPositionData)
                showUpdatePositionDialog = false
            }
        )
    }

    if (showConfirmationDialog != null) {
        ConfirmationDialog(showConfirmationDialog!!) {
            showConfirmationDialog = null
        }
    }
}