package com.robsonribeiro.component.connectionComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ConnectWithoutContact
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.robsonribeiro.component.BentoComponent
import com.robsonribeiro.model.*
import com.robsonribeiro.values.BlueRoyalDarker
import com.robsonribeiro.values.Padding
import com.robsonribeiro.values.RedPantoneDarker
import com.robsonribeiro.values.empty
import kommradiuslocationppd.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConnectionComponent(
    modifier: Modifier,
    user: User? = null,
    connectionData: ConnectionData? = null,
    onJoinClick: ()-> Unit,
    updatePosition: () -> Unit,
    toggleConnection: (Boolean) -> Unit,
    disconnectedClick: () -> Unit
) {
    BentoComponent(
        modifier = modifier
            .padding(
                start = Padding.large,
                top = Padding.large,
                end = Padding.large
            )
            .wrapContentSize()
            .background(color = Color.Transparent)
    ) {
        if (connectionData.isNull()) {
            UnconnectedUser(onClick = onJoinClick)
        } else {
            ConnectionInfo(
                user = user!!,
                connectionData = connectionData!!,
                updatePosition = updatePosition,
                toggleConnection = toggleConnection,
                disconnectedClick = disconnectedClick
            )
        }
    }
}

@Composable
private fun ConnectionInfo(
    modifier: Modifier = Modifier,
    user: User,
    connectionData: ConnectionData,
    updatePosition: ()-> Unit,
    toggleConnection: (Boolean)-> Unit,
    disconnectedClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(Padding.regular),
        verticalArrangement = Arrangement.spacedBy(Padding.large, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Padding.regular, Alignment.CenterHorizontally)
            ) {
                InfoIcon(Icons.Default.AccountCircle)
                Text(
                    text = user.id,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold).blueDarker()
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Padding.regular, Alignment.CenterHorizontally)
            ) {
                InfoIcon(Icons.Outlined.Cloud)
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(Res.string.connection_rmi_info, connectionData.rmiDisplay()),
                        style = MaterialTheme.typography.button.blueDarker()
                    )
                    Text(
                        text = stringResource(Res.string.connection_broker_info, connectionData.brokerDisplay()),
                        style = MaterialTheme.typography.button.blueDarker()
                    )
                }
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                modifier = Modifier.clickable {
                    updatePosition()
                },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.user_location_info),
                    style = MaterialTheme.typography.subtitle2.blueDarker()
                )
                Text(
                    text = user.positionDisplay(),
                    style = MaterialTheme.typography.subtitle1.blueDarker()
                )
            }

            Column(
                modifier = Modifier.clickable {
                    updatePosition()
                },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.user_radius_range_info),
                    style = MaterialTheme.typography.subtitle2.blueDarker()
                )
                Text(
                    text = user.distanceRadius.toString(),
                    style = MaterialTheme.typography.subtitle1.blueDarker()
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(if (user.isOnline) Res.string.user_connected else Res.string.user_disconnected),
                    style = MaterialTheme.typography.subtitle2.blueDarker()
                )
                Switch(
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Green,
                        uncheckedThumbColor = Color.BlueRoyalDarker
                    ),
                    checked = user.isOnline,
                    onCheckedChange = { checked -> toggleConnection(checked) }
                )
            }

            Button(
                onClick = disconnectedClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.RedPantoneDarker,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(Res.string.disconnect_from_server),
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}


@Composable
private fun UnconnectedUser(
    modifier: Modifier = Modifier,
    onClick: ()-> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(Padding.regular)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Padding.regular, Alignment.CenterHorizontally)
    ) {
        InfoIcon(Icons.Default.ConnectWithoutContact)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.connect_client_title),
                style = MaterialTheme.typography.h6.blueDarker()
            )
            Text(
                text = stringResource(Res.string.connect_client_description),
                style = MaterialTheme.typography.subtitle2.blueDarker()
            )
        }
    }
}


@Composable
private fun InfoIcon(icon: ImageVector, size: Dp = Padding.largeExtra) {
    Icon(
        modifier = Modifier.size(Padding.largeExtra),
        imageVector = icon,
        contentDescription = String.empty,
        tint = Color.BlueRoyalDarker
    )
}
private fun TextStyle.blueDarker(color: Color = Color.BlueRoyalDarker) = this.copy(color = Color.BlueRoyalDarker)