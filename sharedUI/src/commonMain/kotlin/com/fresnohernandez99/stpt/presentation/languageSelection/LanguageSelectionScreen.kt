package com.fresnohernandez99.stpt.presentation.languageSelection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.domain.model.LanguagesInPref
import com.fresnohernandez99.stpt.presentation.components.AppScaffold
import com.fresnohernandez99.stpt.presentation.components.BackTopBar
import com.fresnohernandez99.stpt.presentation.languageSelection.component.LanguageItem
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.presentation.navigation.LocalNavController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionScreen(
    viewModel: LanguageSelectionViewModel = koinViewModel(),
    link: Destination.LanguageSelection,
    languages: ImmutableList<Language> = Language.list.toImmutableList()
) {
    val navController = LocalNavController.current

    val languagePref by viewModel.selectedLanguage.collectAsState(
        initial = LanguagesInPref(
            Language.Detect, Language.English
        )
    )

    val current =
        if (link.intent == Destination.LanguageSelection.SOURCE) languagePref.sourceLanguage else languagePref.targetLanguage

    val searchQuery = rememberTextFieldState()
    val filteredLanguages by remember(searchQuery.text, languages) {
        derivedStateOf {
            languages.filter {
                it.name.contains(searchQuery.text, ignoreCase = true) ||
                        it.code.contains(searchQuery.text, ignoreCase = true)
            }
        }
    }

    AppScaffold(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        topBar = {
            BackTopBar(
                onBack = { navController.navigateUp() },
                title = "All Languages",
                modifier = Modifier
            )
        },
    ) { pV ->
        LanguageSelectionContent(
            modifier = Modifier.consumeWindowInsets(pV)
                .padding(pV),
            searchQuery = searchQuery,
            filteredLanguages = filteredLanguages.toImmutableList(),
            current = current,
            onClick = { l ->
                viewModel.changeSelection(
                    l,
                    link.intent == Destination.LanguageSelection.SOURCE
                ) {
                    navController.navigateUp()
                }
            }
        )
    }
}

@Composable
fun LanguageSelectionContent(
    modifier: Modifier = Modifier,
    filteredLanguages: ImmutableList<Language>,
    searchQuery: TextFieldState,
    current: Language,
    onClick: (Language) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            state = searchQuery,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Search",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.surface
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "search bar icon",
                    tint = MaterialTheme.colorScheme.surface
                )
            },
            trailingIcon = if (searchQuery.text.isNotEmpty()) {
                {
                    IconButton(onClick = { searchQuery.clearText() }) {
                        Icon(
                            Icons.Default.Close, contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            } else null,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            onKeyboardAction = KeyboardActionHandler {

            },
            shape = MaterialTheme.shapes.medium,
            lineLimits = TextFieldLineLimits.SingleLine,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.surface,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(filteredLanguages) { language ->
                LanguageItem(
                    language = language,
                    isSelected = current.code == language.code,
                    onClick = {
                        onClick(language)
                    }
                )
            }
        }
    }
}