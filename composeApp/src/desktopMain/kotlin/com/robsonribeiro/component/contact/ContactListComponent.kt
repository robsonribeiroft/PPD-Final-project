package com.robsonribeiro.component.contact

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.robsonribeiro.model.User
import com.robsonribeiro.values.BlackRich
import com.robsonribeiro.values.Padding
import com.robsonribeiro.values.alpha

@Composable
fun ContactListComponent(
    modifier: Modifier,
    users: List<User>,
    onItemClick: (User) -> Unit
) {

    val listState = rememberLazyListState()

    Box(
        modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(Padding.small),
            state = listState,
        ) {
            items(users) { contact ->
                ContactItemListComponent(user = contact, onClick = onItemClick)
            }
            item {
                if (users.isNotEmpty()) {
                    Box(Modifier.size(Padding.veryExtraLarge))
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            style = LocalScrollbarStyle.current.copy(
                unhoverColor = Color.BlackRich.alpha(0.5f),
                hoverColor = Color.White
            ),
            adapter = rememberScrollbarAdapter(
                scrollState = listState
            )
        )
    }
}