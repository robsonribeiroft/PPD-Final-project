package com.robsonribeiro.komms.model

import com.robsonribeiro.model.User
import com.robsonribeiro.values.empty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
sealed interface MessagePayload : java.io.Serializable

@Serializable
@SerialName("CHAT_MESSAGE")
data class ChatMessagePayload(
    val message: String
) : MessagePayload

@Serializable
@SerialName("USER")
data class UserPayload(
    val user: User
) : MessagePayload

fun User.asPayload() = KommData(data = UserPayload(this))

@Serializable
@SerialName("USERS_UPDATED")
data class UsersUpdatedPayload(
    val users: List<User>
) : MessagePayload


@Serializable
@SerialName("COMMAND")
data class CommandPayload(
    val command: String,
    val args: List<String> = emptyList()
) : MessagePayload

@Serializable
data class KommData(
    val data: MessagePayload,
    val senderUserId: String = String.empty,
    val destinationUserId: String = String.empty,
): java.io.Serializable

fun KommData.toJson() = Json.encodeToString(this)

fun String.decodeJson(): KommData = Json.decodeFromString<KommData>(this)

@Suppress("UNCHECKED_CAST")
operator fun <T : MessagePayload> KommData.invoke(block: (Triple<String, String, T>)->Unit) {
    block(Triple(senderUserId, destinationUserId, data as T))
}
