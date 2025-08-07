package com.robsonribeiro.component.contact

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.robsonribeiro.model.*
import com.robsonribeiro.values.*

@Composable
fun ContactItemListComponent(
    contact: User,
    user: User?,
    modifier: Modifier = Modifier,
    onClick: (User) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Padding.regular),
        horizontalAlignment =  Alignment.Start
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = Padding.small)
                .clickable {
                    onClick(contact)
                },
            backgroundColor = if (contact.isOnline) Color.White else Color.BlackRich.alpha(0.5f),
            elevation = Padding.none,
            shape = RoundedCornerShape(Padding.regular),
            border = BorderStroke(
                Padding.single, Color.BaseBackground
            ),
        ) {

            Row(
                modifier = Modifier
                    .padding(Padding.large),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Padding.regular, Alignment.Start)
            ) {
                Icon(
                    modifier = Modifier
                        .size(Padding.largeExtra)
                        .background(
                            shape = CircleShape,
                            color = if (contact.isOnline) Color.GreenEmerald else Color.RedPantoneDarker
                        )
                        .padding(Padding.small),
                    imageVector = Icons.Default.Person,
                    contentDescription = String.empty,
                    tint = if (contact.isOnline) Color.White else Color.BlackRich
                )

                Column {
                    Text(
                        text ="[${contact.id}]",
                        style = MaterialTheme.typography.body1.copy(color = Color.BlackRich)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Padding.large, Alignment.Start)
                    ) {
                        Text(
                            text = "Position(x,y): ${contact.positionDisplay()}",
                            style = MaterialTheme.typography.body2.copy(color = Color.BlackRich)
                        )
                        Text(
                            text = "Radius Range: ${contact.rangeRadiusDisplay()}",
                            style = MaterialTheme.typography.body2.copy(color = Color.BlackRich)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Padding.large, Alignment.Start)
                    ) {
                        Text(
                            text = "Distance: ${contact.distanceFromUser(user!!)}",
                            style = MaterialTheme.typography.body2.copy(color = Color.BlackRich)
                        )
                        Text(
                            text = "Within Reach: ${contact.withinReach(user)}",
                            style = MaterialTheme.typography.body2.copy(color = Color.BlackRich)
                        )
                    }
                }
            }
        }
    }
}

