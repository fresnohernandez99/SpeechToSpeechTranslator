package com.fresnohernandez99.stpt.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.fresnohernandez99.stpt.domain.model.Language
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.change
import speechtospeechtranslator.sharedui.generated.resources.detect_language

@Composable
fun LanguageSelectorBtn(
    selectedLanguage: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicText(
        modifier = modifier.clickable { onClick() }.fillMaxWidth().padding(horizontal = 8.dp),
        text = if (selectedLanguage == Language.Detect) {
            stringResource(Res.string.detect_language)
        } else {
            selectedLanguage.name
        },
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center
        ),
        autoSize = TextAutoSize.StepBased(maxFontSize = 20.sp, minFontSize = 8.sp),
        maxLines = 1
    )
}

@Composable
fun LanguageSelector(
    selectedLanguage: Language,
    languages: ImmutableList<Language>,
    onLanguageSelected: (Language) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.clickable { showDialog = true }) {
        BasicText(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            text = if (selectedLanguage == Language.Detect) {
                stringResource(Res.string.detect_language)
            } else {
                selectedLanguage.name
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center
            ),
            autoSize = TextAutoSize.StepBased(maxFontSize = 20.sp, minFontSize = 8.sp),
            maxLines = 1
        )

        if (showDialog) {
            LanguagePickerDialog(
                languages = languages,
                onLanguageSelected = {
                    onLanguageSelected(it)
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagePickerDialog(
    languages: ImmutableList<Language>,
    onLanguageSelected: (Language) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredLanguages = remember(searchQuery, languages) {
        languages.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.code.contains(searchQuery, ignoreCase = true)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Language",
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search language...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    } else null,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredLanguages) { language ->
                        LanguageItem(
                            language = language,
                            onClick = { onLanguageSelected(language) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageItem(
    language: Language,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = language.flag,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.width(40.dp)
            )
            Text(
                text = if (language == Language.Detect) {
                    stringResource(Res.string.detect_language)
                } else {
                    language.name
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LanguageSelectorTopBar(
    modifier: Modifier = Modifier,
    sourceLanguage: Language,
    targetLanguage: Language,
    onSelectSourceLanguage: () -> Unit,
    onSelectTargetLanguage: () -> Unit,
    swapLanguages: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.elevatedCardElevation(5.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageSelectorBtn(
                selectedLanguage = sourceLanguage,
                modifier = Modifier.weight(1f),
                onClick = onSelectSourceLanguage
            )

            IconButton(
                onClick = swapLanguages,
                enabled = sourceLanguage != Language.Detect && sourceLanguage != targetLanguage,
                shapes = IconButtonShapes(
                    shape = MaterialTheme.shapes.large,
                    pressedShape = MaterialTheme.shapes.small
                ),
                modifier = Modifier,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.change),
                    contentDescription = "Change language ico",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }

            LanguageSelectorBtn(
                selectedLanguage = targetLanguage,
                modifier = Modifier.weight(1f),
                onClick = onSelectTargetLanguage
            )
        }
    }
}
