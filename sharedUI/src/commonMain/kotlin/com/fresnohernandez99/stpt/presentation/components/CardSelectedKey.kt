package com.fresnohernandez99.stpt.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.StyleScope
import androidx.compose.foundation.style.StyleStateKey
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * Clave de estado personalizada para gestionar la selección mediante Styles API.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
val CardSelectedKey = StyleStateKey(defaultValue = false)

/**
 * Extensión para aplicar estilos basados en la selección.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
fun StyleScope.onSelected(block: StyleScope.() -> Unit) {
    state(CardSelectedKey, Style(block)) { key, state ->
        state[key]
    }
}

/**
 * Una alternativa personalizada a Card que utiliza Modifier.styleable para gestionar
 * su apariencia y animaciones.
 *
 * @param isSelected Define si la tarjeta está en estado seleccionado.
 * @param onClick Callback para gestionar el click.
 * @param modifier Modificador para aplicar al contenedor.
 * @param shape Forma de la tarjeta.
 * @param elevation Elevación (simulada mediante dropShadow en la Styles API).
 * @param content Contenido de la tarjeta.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun StyleApiCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 2.dp,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.primaryContainer,
    content: @Composable () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    val styleState = remember { MutableStyleState(interactionSource) }

    LaunchedEffect(isSelected) {
        styleState[CardSelectedKey] = isSelected
    }

    Box(
        modifier = modifier
            .styleable(styleState) {
                background(unselectedColor)
                shape(shape)

                if (elevation > 0.dp) {
                    dropShadow(
                        Shadow(
                            color = Color.Black.copy(alpha = 0.15f),
                            offset = DpOffset(0.dp, elevation),
                            radius = elevation * 2
                        )
                    )
                }

                onSelected {
                    animate(tween(durationMillis = 300)) {
                        background(selectedColor)
                    }
                }
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
