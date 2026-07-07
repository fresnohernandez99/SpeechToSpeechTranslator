package com.fresnohernandez99.stpt.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.modelDownloader.NO_MODEL_SELECTION
import com.fresnohernandez99.stpt.modelDownloader.OPTIMIZED_MODEL_SELECTION
import com.fresnohernandez99.stpt.presentation.components.AppScaffold
import com.fresnohernandez99.stpt.presentation.components.BackTopBar
import com.fresnohernandez99.stpt.presentation.home.components.LanguagePickerDialog
import com.fresnohernandez99.stpt.presentation.modelSelection.ModelOption
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.presentation.navigation.LocalNavController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.language_and_region
import speechtospeechtranslator.sharedui.generated.resources.language_used_for_voice_transcription
import speechtospeechtranslator.sharedui.generated.resources.manage_dictionaries
import speechtospeechtranslator.sharedui.generated.resources.offline_translation_models
import speechtospeechtranslator.sharedui.generated.resources.optimized_model_desc
import speechtospeechtranslator.sharedui.generated.resources.optimized_model_size
import speechtospeechtranslator.sharedui.generated.resources.optimized_model_title
import speechtospeechtranslator.sharedui.generated.resources.select_language
import speechtospeechtranslator.sharedui.generated.resources.settings
import speechtospeechtranslator.sharedui.generated.resources.standard_model_desc
import speechtospeechtranslator.sharedui.generated.resources.standard_model_size
import speechtospeechtranslator.sharedui.generated.resources.standard_model_title
import speechtospeechtranslator.sharedui.generated.resources.transcription_language
import speechtospeechtranslator.sharedui.generated.resources.transcription_model_selection

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    link: Destination.Settings,
) {
    val navHostController = LocalNavController.current

    val language by viewModel.language.collectAsState(Language.list.first().code)
    val modelSavedSelection = viewModel.modelSavedSelection.collectAsState(
        NO_MODEL_SELECTION
    ).value

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    AppScaffold(
        topBar = {
            BackTopBar(
                title = stringResource(Res.string.settings),
                onBack = { navHostController.navigateUp() },
                containerColor = MaterialTheme.colorScheme.primary
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
    ) { paddingValues ->
        SettingsContent(
            Modifier.consumeWindowInsets(paddingValues).padding(paddingValues),
            language = language,
            showDialog = showDialog,
            setShowDialog = setShowDialog,
            modelSavedSelection = modelSavedSelection,
            onLanguageSelected = viewModel::onLanguageSelected,
        )
    }
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    language: String,
    modelSavedSelection: Int,
    onLanguageSelected: (Language) -> Unit
) {
    val navHostController = LocalNavController.current

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item {
                LanguageRegionSection(
                    navigateToLanguages = {
                        setShowDialog(true)
                    },
                    selectedLanguage = language,
                    list = Language.getFilteredLanguages(
                        Language.list,
                        priorityLanguage = Language.Detect
                    ).toImmutableList()
                )
            }

            item {
                LanguageModelSelectionSection(
                    navigateToModelSelection = { navHostController.navigate(Destination.ModelSelection) },
                    modelSavedSelection = modelSavedSelection
                )
            }

            item {
                DictionariesSection(
                    onNavigateToDicts = { navHostController.navigate(Destination.DictsManage) }
                )
            }
        }

        if (showDialog) {
            LanguagePickerDialog(
                languages = Language.getFilteredLanguages(
                    Language.list,
                    priorityLanguage = Language.Detect
                ).toImmutableList(),
                onLanguageSelected = {
                    onLanguageSelected(it)
                    setShowDialog(false)
                },
                onDismiss = { setShowDialog(false) }
            )
        }
    }
}

@Composable
private fun LanguageRegionSection(
    navigateToLanguages: () -> Unit,
    list: ImmutableList<Language>,
    selectedLanguage: String
) {
    Column {
        Text(
            text = stringResource(Res.string.language_and_region),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        TranscriptionLanguageItem(
            navigateToLanguages = navigateToLanguages,
            selectedLanguage = selectedLanguage,
            list = list
        )
    }
}

@Composable
fun TranscriptionLanguageItem(
    navigateToLanguages: () -> Unit,
    list: ImmutableList<Language>,
    selectedLanguage: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(Res.string.transcription_language),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = stringResource(Res.string.language_used_for_voice_transcription),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navigateToLanguages() }
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(12.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedLanguage.uppercase().take(2),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Text(
                        text = list.firstOrNull { it.code == selectedLanguage }?.name
                            ?: Language.Spanish.name,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(Res.string.select_language),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun LanguageModelSelectionSection(
    navigateToModelSelection: () -> Unit,
    modelSavedSelection: Int
) {
    Column {
        Text(
            text = stringResource(Res.string.transcription_model_selection),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        SettingsModelOptionCard(
            model = when (modelSavedSelection) {
                OPTIMIZED_MODEL_SELECTION -> {
                    ModelOption(
                        title = stringResource(Res.string.optimized_model_title),
                        description = stringResource(Res.string.optimized_model_desc),
                        size = stringResource(Res.string.optimized_model_size)
                    )
                }

                else -> {
                    ModelOption(
                        title = stringResource(Res.string.standard_model_title),
                        description = stringResource(Res.string.standard_model_desc),
                        size = stringResource(Res.string.standard_model_size)
                    )
                }
            },
            onClick = {
                navigateToModelSelection()
            },
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun DictionariesSection(
    onNavigateToDicts: () -> Unit
) {
    Column {
        Text(
            text = stringResource(Res.string.manage_dictionaries),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToDicts() }
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(12.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = stringResource(Res.string.manage_dictionaries),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(Res.string.offline_translation_models),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SettingsModelOptionCard(
    model: ModelOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = model.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = model.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = model.size,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
