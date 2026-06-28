package com.fresnohernandez99.stpt.presentation.languageSelection.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fresnohernandez99.stpt.domain.model.Language
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.detect_language
import speechtospeechtranslator.sharedui.generated.resources.download

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LanguageItem(
    language: Language,
    onClick: () -> Unit,
    wasDownloaded: Boolean = false
) {
    Card(
        shape = MaterialTheme.shapes.extraExtraLarge,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = language.flag,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                modifier = Modifier.width(40.dp)
            )
            Text(
                modifier = Modifier.weight(1F),
                text = if (language == Language.Detect) {
                    stringResource(Res.string.detect_language)
                } else {
                    language.name
                },
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.surface
            )

            if (!wasDownloaded)
                Icon(
                    painter = painterResource(Res.drawable.download),
                    contentDescription = "download access",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(24.dp)
                )
        }
    }
}