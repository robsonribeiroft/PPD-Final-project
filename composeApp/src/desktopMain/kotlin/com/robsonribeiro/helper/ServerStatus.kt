package com.robsonribeiro.helper

import com.robsonribeiro.model.ConnectionData
import java.rmi.NotBoundException
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry

fun isRmiServerLive(data: ConnectionData): Boolean {
    return try {
        val registry = LocateRegistry.getRegistry(data.rmiHost, data.rmiPort)
        registry.lookup(data.rmiServerName)
        true
    } catch (_: NotBoundException) {
        false
    } catch (e: RemoteException) {
        println("RMI connection error: Could not connect to registry at ${data.rmiHost}:${data.rmiPort}. ${e.message}")
        false
    } catch (e: Exception) {
        println("An unexpected error occurred while checking RMI status: ${e.message}")
        e.printStackTrace()
        false
    }
}