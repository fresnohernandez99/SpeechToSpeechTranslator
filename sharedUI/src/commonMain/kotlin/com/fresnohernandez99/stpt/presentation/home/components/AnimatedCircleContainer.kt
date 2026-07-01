package com.fresnohernandez99.stpt.presentation.home.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.StyleStateKey
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationStyleApi::class)
val ExpandedStateKey = StyleStateKey(defaultValue = false)

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun CircularRevealContainer(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    baseColor: Color = Color(0xFF6200EE),
    expandedColor: Color = Color(0xFF03DAC5),
    offsetInY: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val styleState = rememberUpdatedStyleState(interactionSource = null) { state ->
        state[ExpandedStateKey] = isExpanded
    }

    Box(
        modifier = modifier.clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .styleable(styleState) {
                    background(baseColor)
                    shape(CircleShape)
                    scale(0.2f)
                    top(offsetInY)

                    transformOrigin(TransformOrigin(0.5f, 1.0f))

                    state(ExpandedStateKey, Style {
                        animate(tween(durationMillis = 1600)) {
                            background(expandedColor)
                            scale(3.5f)

                            shape(RectangleShape)
                        }
                    }) { key, state -> state[key] }
                }
        )

        Box(
            Modifier.styleable(styleState) {
                scale(0f)
                state(ExpandedStateKey, Style {
                    animate(tween(durationMillis = 600)) {
                        scale(1f)
                    }
                }) { key, state -> state[key] }
            },
            contentAlignment = Alignment.Center) {
            content()
        }
    }
}

@Composable
@Preview
fun ScreenWithCorrectStylesApi() {
    var isExpandedByParam by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = { isExpandedByParam = !isExpandedByParam }) {
            Text(if (isExpandedByParam) "Retraer" else "Expandir")
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularRevealContainer(
                isExpanded = isExpandedByParam,
                modifier = Modifier.size(250.dp).offset(y = 50.dp),
                baseColor = Color(0xFF212121),
                expandedColor = Color(0xFFE91E63)
            ) {
                Text(
                    text = if (isExpandedByParam) "¡Listo!" else "Presiona arriba",
                    color = Color.White
                )
            }
        }
    }
}
