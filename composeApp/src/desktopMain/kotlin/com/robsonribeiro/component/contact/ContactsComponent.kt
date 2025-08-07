package com.robsonribeiro.component.contact

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import com.robsonribeiro.component.BentoComponent
import com.robsonribeiro.model.User
import com.robsonribeiro.values.BlueRoyalDarker
import com.robsonribeiro.values.Padding
import com.robsonribeiro.values.empty

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ContactsComponent(
    users: List<User>,
    modifier: Modifier = Modifier,
    onContactItemClick: (User) -> Unit = {}
) {

    var isHoverOverNewContactButton by remember { mutableStateOf(false) }

    BentoComponent(
        modifier = modifier
            .fillMaxSize()
            .padding(Padding.large)
            .background(color = Color.Transparent)
    ) {
        ContactListComponent(
            modifier = Modifier.fillMaxSize()
                .background(color = Color.Transparent),
            users = users,
            onItemClick = onContactItemClick
        )

//        FloatingActionButton(
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(Padding.regular),
//            backgroundColor = Color.BlueRoyalDarker,
//            onClick = {
//                println("New Contact")
//            }
//        ) {
//            Row(
//                modifier = Modifier
//                    .padding(Padding.regular)
//                    .border(
//                        border = if (isHoverOverNewContactButton)
//                            BorderStroke(Padding.single, Color.White)
//                        else BorderStroke(Padding.none, Color.Transparent),
//                        shape = RoundedCornerShape(Padding.large)
//                    )
//                    .onPointerEvent(eventType = PointerEventType.Enter) { isHoverOverNewContactButton = true }
//                    .onPointerEvent(eventType = PointerEventType.Exit) { isHoverOverNewContactButton = false }
//                    .wrapContentSize()
//                    .padding(
//                        vertical = Padding.regular,
//                        horizontal = Padding.large
//                    ),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(Padding.regular)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.PersonAdd,
//                    contentDescription = String.empty,
//                    tint = Color.White
//                )
//                Text(
//                    text = "New Contact",
//                    style = MaterialTheme.typography.button.copy(color = Color.White)
//                )
//            }
//        }
    }
}