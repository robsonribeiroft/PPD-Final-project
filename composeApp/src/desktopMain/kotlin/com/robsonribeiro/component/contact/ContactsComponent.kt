package com.robsonribeiro.component.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.robsonribeiro.component.BentoComponent
import com.robsonribeiro.model.User
import com.robsonribeiro.values.Padding

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ContactsComponent(
    contacts: List<User>,
    user: User?,
    modifier: Modifier = Modifier,
    onContactItemClick: (User) -> Unit = {}
) {
    BentoComponent(
        modifier = modifier
            .fillMaxSize()
            .padding(Padding.large)
            .background(color = Color.Transparent)
    ) {
        ContactListComponent(
            modifier = Modifier.fillMaxSize()
                .background(color = Color.Transparent),
            contacts = contacts,
            user = user,
            onItemClick = onContactItemClick
        )
    }
}