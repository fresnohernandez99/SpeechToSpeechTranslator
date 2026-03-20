package com.fresnohernandez99.stpt.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.presentation.home.HomeUiState
import org.jetbrains.compose.resources.stringResource
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.enter_text
import speechtospeechtranslator.sharedui.generated.resources.translate

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onTextChanged: (String) -> Unit,
    onSourceLanguageSelected: (Language) -> Unit,
    onTargetLanguageSelected: (Language) -> Unit,
    onTranslateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageSelector(
                selectedLanguage = uiState.sourceLanguage,
                languages = uiState.languages,
                onLanguageSelected = onSourceLanguageSelected,
                modifier = Modifier.weight(1f)
            )
            
            Text("→", fontWeight = FontWeight.Bold)
            
            LanguageSelector(
                selectedLanguage = uiState.targetLanguage,
                languages = Language.getFilteredLanguages(
                    allLanguages = uiState.languages,
                    priorityLanguage = Language.Spanish,
                    excludeLanguages = listOf(Language.Detect)
                ),
                onLanguageSelected = onTargetLanguageSelected,
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedTextField(
            value = uiState.textToTranslate,
            onValueChange = onTextChanged,
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            placeholder = { Text(stringResource(Res.string.enter_text)) },
            label = { Text("Text to translate") }
        )

        Button(
            onClick = onTranslateClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.textToTranslate.isNotBlank() && !uiState.isTranslating
        ) {
            if (uiState.isTranslating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(stringResource(Res.string.translate), color = Color.White)
            }
        }

        if (uiState.translatedText.isNotEmpty() || uiState.isTranslating) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
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
                    
                    if (uiState.isTranslating) {
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
