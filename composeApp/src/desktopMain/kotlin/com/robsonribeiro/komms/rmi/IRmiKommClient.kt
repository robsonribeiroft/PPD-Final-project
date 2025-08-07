package com.robsonribeiro.komms.rmi

import com.robsonribeiro.komms.model.KommData
import java.rmi.Remote
import java.rmi.RemoteException

interface IRmiKommClient: Remote {
    @Throws(RemoteException::class)
    fun onChatMessage(payload: KommData)
    @Throws(RemoteException::class)
    fun onNewUserJoined(payload: KommData)
    @Throws(RemoteException::class)
    fun onUserLeft(payload: KommData)
    @Throws(RemoteException::class)
    fun onUsersUpdated(payload: KommData)
    @Throws(RemoteException::class)
    fun onUpdateUserInfo(payload: KommData)
}