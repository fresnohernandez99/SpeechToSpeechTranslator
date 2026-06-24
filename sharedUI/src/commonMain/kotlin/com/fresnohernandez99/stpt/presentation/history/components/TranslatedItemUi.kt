package com.fresnohernandez99.stpt.presentation.history.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.clock

@Composable
fun SimpleTranslatedItemUi(
    modifier: Modifier = Modifier,
    translatedItemOriginalText: String
) {
    Row(modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)) {
        Text(
            translatedItemOriginalText,
            modifier = Modifier.weight(1F),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.tertiary,
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis
        )

        Icon(
            painterResource(Res.drawable.clock),
            contentDescription = "Clock from history item"
        )
    }
}