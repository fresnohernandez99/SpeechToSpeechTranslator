package com.fresnohernandez99.stpt.presentation.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.fresnohernandez99.stpt.presentation.home.HomeUiState
import com.fresnohernandez99.stpt.presentation.home.TranslateState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.enter_text
import speechtospeechtranslator.sharedui.generated.resources.translate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    onTextChanged: (String) -> Unit,
    onTranslateClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabledTranslationFunction: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isFocused by interactionSource.collectIsFocusedAsState()

        val isTypingState by remember(isFocused, uiState.textToTranslate) {
            derivedStateOf {
                isFocused || uiState.textToTranslate.isNotEmpty()
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().weight(0.6F)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = uiState.textToTranslate,
                onValueChange = onTextChanged,
                modifier = Modifier,
                interactionSource = interactionSource,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                minLines = 3,
                singleLine = false,
                maxLines = 8,
                label = {
                    Text(
                        stringResource(Res.string.enter_text),
                        style = if (!isTypingState) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.White,
                    selectionColors = TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.secondary,
                        backgroundColor = Color.White.copy(alpha = 0.3F)
                    )
                )
            )

            IconButton(
                onClick = onTranslateClick,
                modifier = Modifier.align(Alignment.Bottom),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    disabledContentColor = MaterialTheme.colorScheme.surface
                ),
                enabled = enabledTranslationFunction,
                shape = RectangleShape
            ) {
                AnimatedContent(
                    targetState = uiState.translateState,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(durationMillis = 300)) +
                                scaleIn(
                                    initialScale = 0.5f,
                                    animationSpec = tween(300)
                                ) togetherWith
                                fadeOut(animationSpec = tween(durationMillis = 300)) +
                                scaleOut(targetScale = 0.5f, animationSpec = tween(300))
                    },
                    label = "StateIconAnimation"
                ) { targetState ->
                    val rotationModifier = Modifier.rotate(
                        if (targetState == uiState.translateState) 0f else 90f
                    )

                    when (targetState) {
                        TranslateState.NOT_REQUESTED -> {
                            Icon(
                                painter = painterResource(Res.drawable.translate),
                                contentDescription = "Translate language ico",
                                modifier = Modifier.size(50.dp)
                            )
                        }

                        TranslateState.LOADING -> {
                            CircularProgressIndicator(
                                modifier = rotationModifier.size(50.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        }

                        TranslateState.SUCCESS -> {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Translate language completed ico",
                                modifier = Modifier.size(50.dp)
                            )
                        }

                        TranslateState.ERROR -> {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Translate language error ico",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4F)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.extraLarge
                )
        ) {
            if (uiState.translatedText.isNotEmpty() || uiState.translateState in arrayOf(
                    TranslateState.SUCCESS, TranslateState.ERROR, TranslateState.LOADING
                )
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Translation:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (uiState.translateState == TranslateState.LOADING) {
                            Text(
                                text = "Translating...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                            )
                        } else {
                            SelectionContainer {
                                Text(
                                    text = uiState.translatedText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
