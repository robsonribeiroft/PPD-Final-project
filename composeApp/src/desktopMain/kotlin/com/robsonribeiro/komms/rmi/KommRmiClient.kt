package com.robsonribeiro.komms.rmi

import com.robsonribeiro.komms.model.*
import com.robsonribeiro.model.ConnectionData
import com.robsonribeiro.model.User
import java.rmi.NoSuchObjectException
import java.rmi.RemoteException
import java.rmi.server.UnicastRemoteObject

typealias ChatMessageHandler = (String, String) -> Unit
typealias UserHandler = (User) -> Unit
typealias UsersUpdatedHandler = (List<User>) -> Unit

class KommRmiClient(
    private val connectionData: ConnectionData
) {
    private lateinit var user: User
    private lateinit var server: IRmiKommServer
    private lateinit var clientCallback: IRmiKommClient

    fun handshake(user: User, clientCallback: IRmiKommClient) {
        this.user = user
        this.clientCallback = clientCallback
        connect(user)
    }

    private fun connect(user: User) {
        try {
            server = java.rmi.Naming.lookup(connectionData.rmiServiceAddress) as IRmiKommServer
            server.registerClient(user, clientCallback)
            Runtime.getRuntime().addShutdownHook(Thread {
                this.disconnect()
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            server.unregisterClient(user.id)
        } catch (_: NoSuchObjectException) {
            println("Rmi server already unexported")
        } catch (e: RemoteException) {
            e.printStackTrace()
        } finally {
            try {
                UnicastRemoteObject.unexportObject(clientCallback, true)
            } catch (_: NoSuchObjectException) {
                println("Rmi server already unexported")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendDirectMessage(destinationUserId: String, message: String) {
        val payload = KommData(
            senderUserId = user.id,
            destinationUserId = destinationUserId,
            data = ChatMessagePayload(
                message = message
            )
        )
        server.sendChatMessagePayload(payload)
    }

    fun updateUserInfo(user: User) {
        server.updateUserInfo(userId = user.id, user.asPayload())
    }

    class ClientCallback(
        private val onChatMessage: ChatMessageHandler,
        private val onNewUserJoinedHandler: UserHandler,
        private val onUserLeftHandler: UserHandler,
        private val onUpdateUserInfoHandler: UserHandler,
        private val onUsersUpdatedHandler: UsersUpdatedHandler
    ) : UnicastRemoteObject(), IRmiKommClient {
        override fun onChatMessage(payload: KommData) {
            payload<ChatMessagePayload> { (senderUserId, _, data) ->
                onChatMessage(senderUserId, data.message)
            }
        }

        override fun onNewUserJoined(payload: KommData) {
            payload<UserPayload> { (_, _, data) ->
                onNewUserJoinedHandler(data.user)
            }
        }

        override fun onUserLeft(payload: KommData) {
            payload<UserPayload> { (_, _, data) ->
                onUserLeftHandler(data.user)
            }
        }

        override fun onUsersUpdated(payload: KommData) {
            payload<UsersUpdatedPayload> { (_, _, data) ->
                onUsersUpdatedHandler(data.users)
            }
        }

        override fun onUpdateUserInfo(payload: KommData) {
            payload<UserPayload> { (_, _, data) ->
                onUpdateUserInfoHandler(data.user)
            }
        }
    }
}