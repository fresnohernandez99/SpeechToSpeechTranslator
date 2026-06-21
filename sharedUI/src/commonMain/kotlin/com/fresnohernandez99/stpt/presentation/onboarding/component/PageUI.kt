package com.fresnohernandez99.stpt.presentation.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.fresnohernandez99.stpt.presentation.onboarding.OnboardingPage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.onboarding_image_description

@Composable
fun PageUI(modifier: Modifier = Modifier, page: OnboardingPage) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(200.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = page.title,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = page.description,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painterResource(page.imageRes),
            contentDescription = stringResource(Res.string.onboarding_image_description),
            modifier = Modifier.weight(1F).fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun PageUIBig(modifier: Modifier = Modifier, page: OnboardingPage) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LEFT
        Column(Modifier.weight(1F)) {
            Image(
                painterResource(page.imageRes),
                contentDescription = stringResource(Res.string.onboarding_image_description),
                modifier = Modifier.fillMaxHeight().align(Alignment.Start)
            )
        }

        // RIGHT
        Column(Modifier.weight(1F), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = page.title,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = page.description,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}