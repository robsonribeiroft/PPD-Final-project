package com.robsonribeiro.viewmodel

import androidx.lifecycle.ViewModel
import com.robsonribeiro.helper.isRmiServerLive
import com.robsonribeiro.komms.broker.BrokerKomm
import com.robsonribeiro.komms.model.ChatMessagePayload
import com.robsonribeiro.komms.model.invoke
import com.robsonribeiro.komms.rmi.KommRmiClient
import com.robsonribeiro.komms.rmi.KommRmiServer
import com.robsonribeiro.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private var server: KommRmiServer? = null
    private lateinit var client: KommRmiClient
    private lateinit var broker: BrokerKomm

    private val _myUser = MutableStateFlow<User?>(null)
    val myUser = _myUser.asStateFlow()

    private val _connectionData = MutableStateFlow<ConnectionData?>(null)
    val connectionData = _connectionData.asStateFlow()

    private val _contacts = MutableStateFlow<List<User>>(emptyList())
    val contacts = _contacts.asStateFlow()

    private val _chatSessions = MutableStateFlow<ChatSession>(mutableMapOf())
    private val _currentChatUser = MutableStateFlow<User?>(null)
    val currentChatContact = _currentChatUser.asStateFlow()
    private val _currentChatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val currentChatMessages = _currentChatMessages.asStateFlow()

    private val _cachedChatSessions: CachedChatSession = mutableMapOf()


    fun openChatWithContact(user: User) {
        _currentChatUser.value = user
        val cachedMessages = _chatSessions.value[user] ?: emptyList()
        _currentChatMessages.value = cachedMessages
    }

    fun closeChatWithContact() {
        _currentChatUser.value = null
        _currentChatMessages.value = emptyList()
    }

    fun toggleConnection(isOnline: Boolean) {
        val updatedUser = _myUser.value?.copy(isOnline = isOnline)
        updatedUser?.let { client.updateUserInfo(it) }
        if (isOnline) {
            _cachedChatSessions.forEach { userId, messages ->
                messages.forEach { message ->
                    appendIncomingMessage(userId, message)
                }
            }
            _cachedChatSessions.clear()
        }
        _myUser.value = updatedUser
    }

    fun updatePosition(newPositionData: Pair<String, Float>) {
        val newPositionString = newPositionData.first
        val newRageRadius = newPositionData.second
        val cleanValue = newPositionString.trim().replace(" ", "").split(",")
        val x = cleanValue.first().toFloat()
        val y = cleanValue.last().toFloat()
        val updatedUser = _myUser.value?.copy(posX = x, posY = y, distanceRadius = newRageRadius)
        updatedUser?.let { client.updateUserInfo(it) }
        _myUser.value = updatedUser
    }

    fun disconnect() {
        _myUser.value = null
        _connectionData.value = null
        _currentChatUser.value = null
        _currentChatMessages.value = emptyList()
        _contacts.value = emptyList()
        _chatSessions.value = mutableMapOf()
        broker.close()
        server?.shutdown()
    }

    fun connectServer(id: String, brokerHost: String, brokerPort: Int, rmiHost: String, rmiPort: Int) {
        val user =  User(
            id = id,
            posX = 0f,
            posY = 0f,
            distanceRadius = 1f,
            isOnline = true
        )
        val connectionData = ConnectionData(
            rmiHost = rmiHost,
            rmiPort = rmiPort,
            brokerHost = brokerHost,
            brokerPort = brokerPort
        )
        startServer(user,connectionData)
        _myUser.value = user
        _connectionData.value = connectionData
    }

    fun sendDirectMessage(message: String) {
        val sender = _myUser.value ?: return
        val destination = _currentChatUser.value?.let {
                destinationId -> _contacts.value.firstOrNull { user -> user.id == destinationId.id }
        }  ?: return

        if (sender.withinReach(destination) && destination.isOnline) {
            client.sendDirectMessage(destination.id, message)
        } else {
            broker.sendDirectMessage(destination.id, message)
        }
        appendSendedMessage(destination.id, message)
    }


    private fun startServer(
        user: User,
        connectionData: ConnectionData
    ) {
        if (!isRmiServerLive(connectionData)) {
            server = KommRmiServer(connectionData)
            server?.start()
        }
        client = KommRmiClient(connectionData)
        client.handshake(user, KommRmiClient.ClientCallback(
            onChatMessage = { senderId, message ->
                println("Rmi: [${senderId}] > $message")
                appendIncomingMessage(senderId, message)
                println("sessions: ${_chatSessions.value}")
                println("chatUser: ${_currentChatUser.value}")
                println("messageHistory: ${_currentChatMessages.value}")
            },
            onNewUserJoinedHandler = { user ->
                println("onNewUserJoinedHandler: $user")
                _chatSessions.value[user] = emptyList()
            },
            onUserLeftHandler = { user ->
                _chatSessions.value.remove(user)
            },
            onUsersUpdatedHandler = { users ->
                val filtered = users.pluck(_myUser.value)
                _contacts.value = filtered

                val currentSessions: ChatSession = _chatSessions.value
                filtered.forEach { user ->
                    val exists = currentSessions.keys.any { it.id == user.id }
                    if (!exists) {
                        currentSessions[user] = emptyList()
                    }
                }
                _chatSessions.value = currentSessions
            },
            onUpdateUserInfoHandler = { updatedUser ->
                val currentSessions = _chatSessions.value.toMutableMap()
                val oldUser = currentSessions.keys.firstOrNull { it.id == updatedUser.id }

                if (oldUser != null) {
                    val messages = currentSessions.remove(oldUser) ?: emptyList()
                    currentSessions[updatedUser] = messages
                    _chatSessions.value = currentSessions
                    _contacts.value = currentSessions.keys.toList().pluck(_myUser.value)
                    if (_currentChatUser.value?.id == updatedUser.id) {
                        _currentChatUser.value = updatedUser
                    }
                }
            }
        ))
        broker = BrokerKomm(
            user.id,
            connectionData
        ) { event ->
            event<ChatMessagePayload> { (senderUserId, _, data) ->
                if (_myUser.value?.isOnline ?: false) {
                    appendIncomingMessage(senderUserId, data.message)
                } else {
                    val updatedMessages = (_cachedChatSessions[senderUserId] ?: emptyList()) + data.message
                    _cachedChatSessions[senderUserId] = updatedMessages
                }
            }
        }
    }

    fun appendIncomingMessage(senderId: String, message: String) {
        val userKey = _chatSessions.value.keys.firstOrNull { user -> user.id == senderId } ?: return
        val newMessage = ChatMessage(sender = senderId, message = message)
        val updatedMessagesSession = (_chatSessions.value[userKey] ?: emptyList()) + newMessage
        _chatSessions.value[userKey] = updatedMessagesSession
        if (_currentChatUser.value?.id == userKey.id) {
            _currentChatMessages.value = updatedMessagesSession
        }
    }

    fun appendSendedMessage(destinationId: String, message: String) {
        val userKey = _chatSessions.value.keys.firstOrNull { user -> user.id == destinationId } ?: return
        val newMessage = ChatMessage(sender = _myUser.value?.id ?: "You", message = message, messageOwner = TypeMessage.OWNER)
        val updatedMessagesSession = (_chatSessions.value[userKey] ?: emptyList()) + newMessage
        _chatSessions.value[userKey] = updatedMessagesSession
        if (_currentChatUser.value?.id == destinationId) {
            _currentChatMessages.value = updatedMessagesSession
        }
    }
}
