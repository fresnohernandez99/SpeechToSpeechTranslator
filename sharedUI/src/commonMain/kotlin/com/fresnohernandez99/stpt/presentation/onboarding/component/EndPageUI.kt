package com.fresnohernandez99.stpt.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EndPageUI(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().background(
            brush = Brush.horizontalGradient(
                listOf(
                    Color.Black.copy(alpha = 0.3F),
                    Color.Black.copy(alpha = 0.05F),
                    Color.Black.copy(alpha = 0.05F),
                    Color.Black.copy(alpha = 0.05F)
                )
            )
        ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Ready!",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
    }
}