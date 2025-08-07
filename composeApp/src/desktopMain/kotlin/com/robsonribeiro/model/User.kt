package com.robsonribeiro.model

import kotlinx.serialization.Serializable
import kotlin.math.pow
import kotlin.math.sqrt

@Serializable
data class User(
    val id: String,
    val posX: Float,
    val posY: Float,
    val distanceRadius: Float = 10f,
    val isOnline: Boolean = false
): java.io.Serializable

fun User.withinReach(user: User): Boolean {
    val distance = distanceFromUser(user)
    val totalReach = this.distanceRadius + user.distanceRadius
    return totalReach - distance > 0
}

fun User.distanceFromUser(user: User): Float {
    val deltaX = user.posX - this.posX
    val deltaY = user.posY - this.posY
    return sqrt(deltaX.pow(2) + deltaY.pow(2))
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
