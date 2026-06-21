package com.fresnohernandez99.stpt.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fresnohernandez99.stpt.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.app_name
import speechtospeechtranslator.sharedui.generated.resources.loading_shape_1
import speechtospeechtranslator.sharedui.generated.resources.loading_shape_2
import speechtospeechtranslator.sharedui.generated.resources.translate_logo

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                -15f at 0 using FastOutSlowInEasing
                0f at 600 using FastOutSlowInEasing
                0f at 900
                25f at 1500 using FastOutSlowInEasing
                25f at 1800
                -15f at 3000
            }
        )
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(Res.drawable.translate_logo),
                contentDescription = "App Logo"
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        Image(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxSize()
                .graphicsLayer {
                    this.translationY = 1050F
                    this.translationX = -400F
                    this.rotationZ = -rotation * 0.7f // Swing invertido y menor
                    this.scaleX = scale * 1.05f
                    this.scaleY = scale * 1.05f
                },
            painter = painterResource(Res.drawable.loading_shape_2),
            contentDescription = "Floating shape 2"
        )

        Image(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxSize()
                .graphicsLayer {
                    this.translationY = 1000F
                    this.translationX = 200F
                    this.rotationZ = rotation
                    this.scaleX = scale
                    this.scaleY = scale
                },
            painter = painterResource(Res.drawable.loading_shape_1),
            contentDescription = "Floating shape 1"
        )
    }
}

@Preview
@Composable
fun LoadingViewPreview() {
    AppTheme({}) {
        LoadingView()
    }
}
