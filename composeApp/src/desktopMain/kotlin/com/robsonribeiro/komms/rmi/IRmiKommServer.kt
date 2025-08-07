package com.robsonribeiro.komms.rmi

import com.robsonribeiro.komms.model.KommData
import com.robsonribeiro.model.User
import java.rmi.Remote
import java.rmi.RemoteException

interface IRmiKommServer: Remote {
    @Throws(RemoteException::class, IllegalArgumentException::class)
    fun registerClient(user: User, clientCallback: IRmiKommClient)
    @Throws(RemoteException::class)
    fun unregisterClient(userId: String?)
    @Throws(RemoteException::class)
    fun sendChatMessagePayload(payload: KommData)

    @Throws(RemoteException::class)
    fun updateUserInfo(userId: String, payload: KommData)
}