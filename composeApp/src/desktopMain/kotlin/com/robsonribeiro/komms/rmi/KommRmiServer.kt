package com.robsonribeiro.komms.rmi

import com.robsonribeiro.komms.model.KommData
import com.robsonribeiro.komms.model.UsersUpdatedPayload
import com.robsonribeiro.komms.model.asPayload
import com.robsonribeiro.model.ConnectionData
import com.robsonribeiro.model.User
import java.rmi.NoSuchObjectException
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class KommRmiServer(
    private val connectionData: ConnectionData,
) : UnicastRemoteObject(), IRmiKommServer {

    private val clients = ConcurrentHashMap<String, UserServerInfo>()
    private lateinit var registry: Registry

    fun start() {
        try {
            this.registry = LocateRegistry.createRegistry(connectionData.rmiPort)
            println("Registry created on port ${connectionData.rmiPort}. Attempting rebind using name '${connectionData.rmiServerName}' only.")
            java.rmi.Naming.rebind(connectionData.rmiServiceAddress, this@KommRmiServer)
            println("Rebind successful using name only.")
            Runtime.getRuntime().addShutdownHook(thread(start = false) {
                this.shutdown()
            })
        } catch (e: Exception) {
            println("SERVER: Failed to start server: ${e.message}")
            e.printStackTrace()
            exitProcess(1)
        }
    }

    fun shutdown() {
        clients.clear()
        try {
            java.rmi.Naming.unbind(connectionData.rmiServiceAddress)
            unexportObject(this, true)
            println("SERVER: Shutdown complete.")
        } catch (_: NoSuchObjectException) {
            println("SERVER: Rmi server was already disable.")
        } catch (e: Exception) {
            println("SERVER: Error during RMI cleanup: ${e.message}")
            e.printStackTrace()
        }
    }


    override fun registerClient(user: User, clientCallback: IRmiKommClient) {
        if (clients.userExists(user.id)) {
            return
        }
        clients[user.id] = UserServerInfo(
            user,
            clientCallback
        )
        try {
            notifyNewUserJoined(user)
        } catch (e: RemoteException) {
            e.printStackTrace()
            unregisterUserGracefully(
                user = user,
                reason = "SERVER: Error sending welcome notification to '${user.id}'. Removing."
            )
        }
    }

    override fun unregisterClient(userId: String?) {
        if (userId.isNullOrBlank()) return
        val removedClient: UserServerInfo? = clients.remove(userId)
        removedClient?.let {
            notifyUserLeftServer(it.user)
            if (clients.isEmpty()) {
                shutdown()
            }
        }
    }

    override fun sendChatMessagePayload(payload: KommData) {
        clients.withUser(payload.destinationUserId) {
            onChatMessage(payload)
        }
    }

    override fun updateUserInfo(userId: String, payload: KommData) {
        safeBroadcast(userId) {
            onUpdateUserInfo(payload)
        }
    }

    private fun notifyNewUserJoined(user: User) {
        safeBroadcast(user.id) {
            onNewUserJoined(user.asPayload())
        }
        safeBroadcast(user.id, includeAll = true) {
            onUsersUpdated(KommData(UsersUpdatedPayload(clients.users())))
        }
    }

    private fun notifyUserLeftServer(user: User) {
        safeBroadcast(user.id) {
            onUserLeft(user.asPayload())
        }
        safeBroadcast(user.id, includeAll = true) {
            onUsersUpdated(KommData(UsersUpdatedPayload(clients.users())))
        }
    }

    private fun unregisterUserGracefully(user: User, reason: String) {
        println("SERVER: Unregistering '${user.id}' because they $reason.")
        clients.remove(user.id)
    }

    private fun safeBroadcast(senderId: String, includeAll: Boolean = false, block: IRmiKommClient.() ->Unit) {
        val clientsToRemove = mutableListOf<User>()
        clients.forEach { (userId, userServerInfo) ->
            if ( includeAll || userId != senderId ) {
                try {
                    userServerInfo.client.block()
                } catch (e: RemoteException) {
                    println("SERVER: Error sending message to '${userId}'. Assuming disconnected: ${e.message}")
                    clientsToRemove.add(userServerInfo.user)
                }
            }
        }
        clientsToRemove.forEach { unregisterUserGracefully(it, "disconnected abruptly") }
    }

    companion object Companion {
        val serverName: String get() = KommRmiServer::class.java.simpleName
    }
}


private fun ConcurrentHashMap<String, UserServerInfo>.withUser(userId: String, block: IRmiKommClient.()-> Unit) {
    this[userId]?.client?.block()
}

private fun ConcurrentHashMap<String, UserServerInfo>.userExists(userId: String): Boolean {
    return containsKey(userId)
}

private fun ConcurrentHashMap<String, UserServerInfo>.users(): List<User> {
    return values.map { it.user }
}

private data class UserServerInfo(
    var user: User,
    val client: IRmiKommClient
)
