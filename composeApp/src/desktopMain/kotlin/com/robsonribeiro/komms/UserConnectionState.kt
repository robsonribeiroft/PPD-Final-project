package com.robsonribeiro.komms

data class UserConnectionState(
    val clientId: String? = null,
    val host: String? = null,
    val port: Int? = null,
    val isConnected: Boolean = false
)
