package com.fresnohernandez99.stpt.presentation.modelSelection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fresnohernandez99.stpt.modelDownloader.NO_MODEL_SELECTION
import com.fresnohernandez99.stpt.presentation.components.BackTopBar
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.theme.WindowSize
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.optimized_model_desc
import speechtospeechtranslator.sharedui.generated.resources.optimized_model_size
import speechtospeechtranslator.sharedui.generated.resources.optimized_model_title
import speechtospeechtranslator.sharedui.generated.resources.settings_model_selection_description
import speechtospeechtranslator.sharedui.generated.resources.settings_model_selection_text
import speechtospeechtranslator.sharedui.generated.resources.standard_model_desc
import speechtospeechtranslator.sharedui.generated.resources.standard_model_size
import speechtospeechtranslator.sharedui.generated.resources.standard_model_title
import speechtospeechtranslator.sharedui.generated.resources.transcription_model_selection

private const val HIDE_TIME_ELAPSE = 1500L

data class ModelOption(
    val title: String,
    val description: String,
    val size: String = ""
)

@Composable
fun ModelSelectionScreen(
    viewModel: ModelSelectionViewModel = koinViewModel(),
    link: Destination.ModelSelection,
    windowSize: WindowSize,
    navHostController: NavHostController
) {
    val modelOptions = listOf(
        ModelOption(
            title = stringResource(Res.string.standard_model_title),
            description = stringResource(Res.string.standard_model_desc),
            size = stringResource(Res.string.standard_model_size)
        ),
        ModelOption(
            title = stringResource(Res.string.optimized_model_title),
            description = stringResource(Res.string.optimized_model_desc),
            size = stringResource(Res.string.optimized_model_size)
        )
    )

    var selectedModel by remember { mutableIntStateOf(0) } // Standard model selected by default
    var modelSavedSelection by remember { mutableStateOf(NO_MODEL_SELECTION) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        modelSavedSelection = viewModel.modelSavedSelection.first()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BackTopBar(
            onBack = { navHostController.navigateUp() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Title
            Text(
                text = stringResource(Res.string.transcription_model_selection),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Subtitle
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.settings_model_selection_text),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Description
            Text(
                text = stringResource(Res.string.settings_model_selection_description),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Model options
            modelOptions.forEachIndexed { index, model ->
                ModelOptionCard(
                    model = model,
                    isSelected = if (modelSavedSelection != NO_MODEL_SELECTION) {
                        modelSavedSelection == index
                    } else {
                        selectedModel == index
                    },
                    onClick = {
                        selectedModel = index
                        coroutineScope.launch {
                            viewModel.setModelSelection(selectedModel)
                        }
                        navHostController.navigateUp()
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
        // end of content
    }
}

@Composable
fun ModelOptionCard(
    model: ModelOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.primary
        ) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = model.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = model.description,
                fontSize = 14.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (model.size.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = model.size,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
