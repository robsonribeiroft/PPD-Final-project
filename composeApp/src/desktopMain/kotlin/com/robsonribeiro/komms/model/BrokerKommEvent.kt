package com.robsonribeiro.komms.model

sealed interface BrokerKommEvent {
    data class BrokerMessageReceived(
        val senderId: String,
        val message: String,
        val destination: String
    ): BrokerKommEvent
}
