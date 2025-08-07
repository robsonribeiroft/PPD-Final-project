package com.robsonribeiro.komms.broker

import com.robsonribeiro.komms.model.ChatMessagePayload
import com.robsonribeiro.komms.model.KommData
import com.robsonribeiro.model.ConnectionData
import org.apache.activemq.ActiveMQConnection
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.command.ActiveMQQueue
import javax.jms.*

/*
* Run the command to activate the activeMQ
* $ ./activemq console
*/
class BrokerKomm(
    private val userId: String,
    private val connectionData: ConnectionData,
    private val messageCallback: (KommData) -> Unit
) {

    private lateinit var connection: Connection
    private lateinit var session: Session
    private lateinit var directMessageConsumer: MessageConsumer
    private val brokerUrl = "tcp://${connectionData.brokerHost}:${connectionData.brokerPort}"
    private val queueName = userId.getQueueName()

    init {
        setup()
    }
    private fun setup() {
        if (!createConnection()) {
            println("BrokerKomm setup aborted due to connection failure.")
            return
        }
        setupDirectMessageConsumer()
    }

    private fun createConnection(): Boolean {
        return try {
            val connectionFactory = ActiveMQConnectionFactory(brokerUrl)
            connection = connectionFactory.createConnection()
            connection.clientID = userId
            connection.start()

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
            println("Usuário '$userId' conectado ao broker com sucesso.")
            true
        } catch (e: InvalidClientIDException) {
            // "User '$userId' is already connected: ${e.message}"
            e.printStackTrace()
            false
        } catch (e: JMSException) {
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun setupDirectMessageConsumer() {
        try {
            val directQueue = session.createQueue(queueName)
            directMessageConsumer = session.createConsumer(directQueue)
            directMessageConsumer.messageListener = MessageListener { message ->
                when (message) {
                    is TextMessage -> {
                        val sender = message.getStringProperty(DM_PROPERTY_SENDER_ID) ?: "Unknown"
                        messageCallback(
                            KommData(
                                ChatMessagePayload(message.text),
                                senderUserId = sender,
                                destinationUserId = userId
                            )
                        )
                    }
                    else -> {
                        println("UserBroker: Received non-text direct message on '$queueName'.")
                    }
                }
            }
            println("User '$userId' is now listening for direct messages on queue '$queueName'.")
        } catch (e: JMSException) {
             // "Failed to set up direct message consumer for $userId: ${e.message}"
            e.printStackTrace()
        }
    }

    fun sendDirectMessage(destinationUserId: String, message: String) {
        try {
            val destinationQueueName = destinationUserId.getQueueName()
            val destination = session.createQueue(destinationQueueName)
            val producer = session.createProducer(destination)
            producer.deliveryMode = DeliveryMode.PERSISTENT

            val message = session.createTextMessage(message)
            // Add a custom property to identify the sender
            message.setStringProperty(DM_PROPERTY_SENDER_ID, userId)

            producer.send(message)
            producer.close()
            println("User '$userId' sent direct message to '$destinationUserId' on queue '$destinationQueueName'.")
        } catch (e: JMSException) {
            // "Failed to send direct message to $destinationUserId: ${e.message}"
            e.printStackTrace()
        }
    }


    fun close() {
        try {
            directMessageConsumer.close()
            session.close()
            connection.close()

            println("UserBroker for '$userId' closed all JMS resources.")
        } catch (e: JMSException) {
            e.printStackTrace()
            // "Error closing UserBroker for $userId: ${e.message}"

        }
    }

    companion object {
        const val DM_PROPERTY_SENDER_ID = "SENDER_ID"
    }
}

fun String.getQueueName() = "user.queue.$this"

fun Connection?.liveQueues(): Set<ActiveMQQueue> {
    val activeConnection = this as ActiveMQConnection
    val destinationSource = activeConnection.destinationSource
    return destinationSource.queues
}

fun Connection?.queueNameAlreadyExists(queueName: String): Boolean {
    return this.liveQueues().map { it.queueName }.contains(queueName)
}