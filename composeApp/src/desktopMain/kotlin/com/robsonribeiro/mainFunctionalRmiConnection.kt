package com.robsonribeiro

import com.robsonribeiro.helper.isRmiServerLive
import com.robsonribeiro.komms.rmi.KommRmiServer
import com.robsonribeiro.komms.rmi.KommRmiClient
import com.robsonribeiro.model.ConnectionData
import com.robsonribeiro.model.User

fun main() {

    val connectionData = ConnectionData()

    println("${isRmiServerLive(connectionData)}")
    val server = KommRmiServer(connectionData)
    server.start()

    if (isRmiServerLive(connectionData)) {
        println("Server is live and listening on ${connectionData.rmiServiceAddress}.")
    } else {
        println("Server was not initialized!")
    }

    val user1 = User(
        id = "user1",
        posX = 1f,
        posY = 1f,
        isOnline = true
    )
    val client1 = KommRmiClient(
        connectionData
    )
    client1.handshake(user1, KommRmiClient.ClientCallback(
        onChatMessage = { sender, message ->
            println("[${sender}] > $message")
        },
        onNewUserJoinedHandler = { user ->
            println("User ${user.id} joined")
        },
        onUserLeftHandler = { user ->
            println("User ${user.id} left")
        },
        onUsersUpdatedHandler = { users ->
            println(users)
        },
        onUpdateUserInfoHandler = { user -> println(user) }
    ))

    val user2 = user1.copy(id = "user2")
    val client2 = KommRmiClient(
        connectionData
    )
    client2.handshake(user2, KommRmiClient.ClientCallback(
        onChatMessage = { sender, message ->
            println("[${sender}] > $message")
        },
        onNewUserJoinedHandler = { user ->
            println(user)
        },
        onUserLeftHandler = { userId ->
            println(userId)
        },
        onUsersUpdatedHandler = { users ->
            println(users)
        },
        onUpdateUserInfoHandler = { user -> println(user) }
    ))

    client1.sendDirectMessage(user2.id, "Hi 2")
    client2.sendDirectMessage(user1.id, "Hello 1")
    client1.disconnect()
    client2.disconnect()
}

