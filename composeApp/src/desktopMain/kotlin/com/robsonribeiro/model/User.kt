package com.robsonribeiro.model

import kotlinx.serialization.Serializable
import kotlin.math.pow

@Serializable
data class User(
    val id: String,
    val posX: Float,
    val posY: Float,
    val distanceRadius: Float = 10f,
    val isOnline: Boolean = false
): java.io.Serializable

fun User.withinReach(user: User): Boolean {
    if (id == user.id) return false

    val dx = this.posX - user.posX
    val dy = this.posY - user.posY

    val distanceSquared = dx.pow(2) + dy.pow(2)
    val totalReach = this.distanceRadius + user.distanceRadius
    val totalReachSquared = totalReach.pow(2)
    return distanceSquared <= totalReachSquared
}

fun User.positionDisplay(): String {
    return "$posX, $posY"
}

fun User.rangeRadiusDisplay(): String {
    return distanceRadius.toString()
}

fun List<User>.pluck(user: User?): List<User> {
    val userId = user?.id ?: return this
    return filter { it.id != userId }
}

fun User?.isNull() = this == null
fun User?.isNotNull() = this != null

val mockUser = User("MOCKED_USER", -1f,-1f, 1f,true)
