package com.robsonribeiro.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.robsonribeiro.values.Padding
import com.robsonribeiro.values.alpha

@Composable
fun BentoComponent(
    modifier: Modifier,
    backgroundColor: Color = Color.White.alpha(0.3f),
    border: BorderStroke = BorderStroke(Padding.single, Color.White.alpha(0.5f)),
    content: @Composable BoxScope.()->Unit
) {
    Card(
        modifier = modifier,
        elevation = Padding.none,
        shape = RoundedCornerShape(Padding.regular),
        border = border,
        backgroundColor = backgroundColor,

    ) {
        Box(
            modifier = Modifier
                .background(color = Color.Transparent)
        ) { content() }
    }
}