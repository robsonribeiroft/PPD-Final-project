package com.robsonribeiro.model

import com.robsonribeiro.komms.rmi.KommRmiServer

data class ConnectionData(
    val brokerHost: String = DefaultValues.BROKER_HOST_ADDRESS,
    val brokerPort: Int = DefaultValues.BROKER_PORT,
    val rmiHost: String = DefaultValues.RMI_HOST_ADDRESS,
    val rmiPort: Int = DefaultValues.RMI_PORT,
    val rmiServerName: String = KommRmiServer.serverName,
    val isConnected: Boolean = false
) {
    val rmiServiceAddress: String get() = "rmi://$rmiHost:$rmiPort/$rmiServerName"

    object DefaultValues {
        const val BROKER_HOST_ADDRESS = "localhost"
        const val BROKER_PORT = 61616
        const val RMI_HOST_ADDRESS = "localhost"
        const val RMI_PORT = 12345
        const val RMI_SERVER_NAME = "KommRmiServer"
    }
}

fun ConnectionData?.isNull() = this == null

fun ConnectionData.rmiDisplay() = "$rmiHost:$rmiPort/$rmiServerName"
fun ConnectionData.brokerDisplay() = "$brokerHost:$brokerPort"

