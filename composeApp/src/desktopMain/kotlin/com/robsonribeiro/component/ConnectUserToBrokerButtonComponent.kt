package com.robsonribeiro.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import com.robsonribeiro.komms.UserConnectionState
import com.robsonribeiro.values.BlackRich
import com.robsonribeiro.values.GreenEmerald
import com.robsonribeiro.values.Padding
import com.robsonribeiro.values.empty
import kommradiuslocationppd.composeapp.generated.resources.Res
import kommradiuslocationppd.composeapp.generated.resources.ic_add_user
import kommradiuslocationppd.composeapp.generated.resources.ic_server_host
import org.jetbrains.compose.resources.vectorResource

private const val BUTTON_TITLE_CONNECT_CLIENT = "Join the server"
private const val BUTTON_TITLE_CONNECTED_CLIENT = "Welcome,"
private const val SERVER_BUTTON_SERVER_RUNNING = "Server is running on"
private const val BUTTON_CLICK_SETUP = "Click here to setup"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConnectUserToBrokerButtonComponent(
    modifier: Modifier = Modifier,
    userConnectionState: UserConnectionState,
    onClick: ()->Unit
) {

    val infiniteTransition = rememberInfiniteTransition()
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color.Transparent,
        targetValue = Color.GreenEmerald,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    var isHoveringOver by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .onPointerEvent(eventType = PointerEventType.Enter){ isHoveringOver = true }
            .onPointerEvent(eventType = PointerEventType.Exit){ isHoveringOver = false }
            .clickable {
                onClick()
                isHoveringOver = !isHoveringOver
            }
            .border(
                width = Padding.tiny,
                color = if (userConnectionState.isConnected) animatedColor else Color.Transparent,
                shape = RoundedCornerShape(Padding.regular)
            ),
        elevation = if (isHoveringOver) Padding.large else Padding.tiny,
        shape = RoundedCornerShape(Padding.regular)
    ) {
        Row (
            Modifier.padding(
                vertical = Padding.regular,
                horizontal = Padding.regular
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Padding.regular)
        ) {
            UserConnectionUiHandler(
                userConnectionState
            )
        }
    }
}


@Composable
fun UserConnectionUiHandler(
    userConnectionState: UserConnectionState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (userConnectionState.isConnected) {
            Row(
                modifier = Modifier
                    .weight(
                        1f,
                        true
                    )
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Padding.regular)
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_add_user),
                    contentDescription = String.empty,
                    tint = Color.BlackRich,
                )
                Column {
                    Text(
                        text = BUTTON_TITLE_CONNECTED_CLIENT,
                        style = MaterialTheme.typography.overline
                    )
                    Text(
                        text = userConnectionState.clientId.orEmpty(),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
            Row(
                modifier = Modifier
                    .weight(
                        1f,
                        true
                    )
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_server_host),
                    contentDescription = String.empty,
                    tint = Color.BlackRich,
                )
                Column {
                    Text(
                        text = SERVER_BUTTON_SERVER_RUNNING,
                        style = MaterialTheme.typography.subtitle2
                    )
                    Text(
                        text = "${userConnectionState.host}:${userConnectionState.port}",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Padding.large, Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_add_user),
                    contentDescription = String.empty,
                    tint = Color.BlackRich,
                )
                Column {
                    Text(
                        text = BUTTON_TITLE_CONNECT_CLIENT,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = BUTTON_CLICK_SETUP,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}