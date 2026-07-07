package com.fresnohernandez99.stpt.presentation.history.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.StyleStateKey
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fresnohernandez99.stpt.data.local.TranslatedItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.clock
import speechtospeechtranslator.sharedui.generated.resources.history_clock_icon_desc

@Composable
fun SimpleTranslatedItemUi(
    modifier: Modifier = Modifier,
    translatedItemOriginalText: String
) {
    Row(
        modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            translatedItemOriginalText,
            modifier = Modifier.weight(1F).padding(end = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary,
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis
        )

        Icon(
            painterResource(Res.drawable.clock),
            contentDescription = stringResource(Res.string.history_clock_icon_desc),
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(24.dp)
        )
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
private val ExpandedStateKey = StyleStateKey(defaultValue = false)

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun TranslatedItemUi(
    modifier: Modifier = Modifier,
    translatedItem: TranslatedItem,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Column(modifier.clickable { onToggleExpand() }) {
        val styleState = rememberUpdatedStyleState(interactionSource = null) { state ->
            state[ExpandedStateKey] = isExpanded
        }

        Row(
            Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                translatedItem.originalText,
                modifier = Modifier.weight(1F).padding(end = 8.dp),
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
            )

            Icon(
                painterResource(Res.drawable.clock),
                contentDescription = stringResource(Res.string.history_clock_icon_desc),
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(24.dp).styleable(styleState) {
                    rotationZ(-45F)
                    state(ExpandedStateKey, Style {
                        animate(tween(durationMillis = 1600)) {
                            rotationZ(-220F)
                        }
                    }) { key, state -> state[key] }
                }
            )
        }

        Text(
            translatedItem.translatedText,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                // meanings
                translatedItem.dictionaryResponse?.entries?.forEach { entry ->
                    entry.senses.forEach { sense ->
                        Text(
                            sense.definition,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                        )

                        FlowRow(modifier = Modifier.padding(horizontal = 16.dp)) {
                            if (sense.examples.isNotEmpty())
                                Text(
                                    "Ex: ",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                                    color = MaterialTheme.colorScheme.tertiary,
                                )

                            sense.examples.forEach { example ->
                                Text(
                                    example,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                                    color = MaterialTheme.colorScheme.tertiary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
