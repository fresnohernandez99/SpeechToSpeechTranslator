package com.fresnohernandez99.stpt.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.fresnohernandez99.stpt.presentation.components.PageIndicator
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.presentation.navigation.LocalNavController
import com.fresnohernandez99.stpt.presentation.onboarding.component.EndPageUI
import com.fresnohernandez99.stpt.presentation.onboarding.component.PageUI
import com.fresnohernandez99.stpt.presentation.onboarding.component.PageUIBig
import com.fresnohernandez99.stpt.theme.LocalWindowSizeHelper
import com.fresnohernandez99.stpt.theme.WindowSize
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.onboarding_image_1
import speechtospeechtranslator.sharedui.generated.resources.onboarding_image_2
import speechtospeechtranslator.sharedui.generated.resources.onboarding_image_3

@Composable
fun OnboardingScreen(
    link: Destination.Onboarding
) {
    val haptic = LocalHapticFeedback.current
    val navHostController = LocalNavController.current
    val windowSize = LocalWindowSizeHelper.current


    val pages = remember {
        arrayOf(
            OnboardingPage(
                title = "STPT",
                description = "offline translation for Free",
                imageRes = Res.drawable.onboarding_image_1
            ),

            OnboardingPage(
                title = "Different Languages",
                description = "common communication",
                imageRes = Res.drawable.onboarding_image_2
            ),

            OnboardingPage(
                title = "Translate",
                description = "anywhere, anytime",
                imageRes = Res.drawable.onboarding_image_3
            )
        )
    }

    val state = rememberPagerState {
        pages.size + 1
    }
    val currentPageOffset by remember { derivedStateOf { state.currentPageOffsetFraction } }

    var hasTriggered by remember { mutableStateOf(false) }
    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(state.targetPage, currentPageOffset) {
        if (!hasTriggered &&
            state.targetPage == pages.size &&
            currentPageOffset <= 0.5F
        ) {
            haptic.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
            hasTriggered = true
        } else
            if (hasTriggered && !hasNavigated &&
                state.targetPage == pages.size &&
                currentPageOffset <= 0.0F
            ) {
                hasNavigated = true
                navHostController.navigate(Destination.Home()) {
                    popUpTo(Destination.Onboarding) {
                        inclusive = true
                    }
                }
            } else
                if (hasTriggered &&
                    state.targetPage == pages.size - 1
                ) {
                    hasTriggered = false
                }
    }

    when (windowSize) {
        WindowSize.Compact -> {
            OnboardingContent(
                modifier = Modifier,
                state = state,
                pages = pages.toImmutableList()
            )
        }

        WindowSize.Medium, WindowSize.Expanded -> {
            OnboardingContentBig(
                modifier = Modifier,
                state = state,
                pages = pages.toImmutableList()
            )
        }

    }
}

@Composable
fun OnboardingContent(
    modifier: Modifier = Modifier,
    state: PagerState,
    pages: ImmutableList<OnboardingPage>
) {
    Box(
        modifier.background(color = MaterialTheme.colorScheme.primary)
            .systemBarsPadding()
    ) {
        HorizontalPager(
            state = state,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary)
        ) { page ->
            val p = if (page != state.pageCount - 1) pages[page] else null

            if (p == null) {
                EndPageUI(modifier = Modifier)
            } else
                PageUI(modifier = Modifier, page = p)
        }

        PageIndicator(
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
            numberOfPages = state.pageCount,
            selectedPage = state.currentPage,
            defaultRadius = 20.dp,
            selectedLength = 50.dp,
            space = 16.dp,
            animationDurationInMillis = 500,
        )
    }
}


@Composable
fun OnboardingContentBig(
    modifier: Modifier = Modifier,
    state: PagerState,
    pages: ImmutableList<OnboardingPage>
) {
    Box(
        modifier.background(color = MaterialTheme.colorScheme.primary)
            .systemBarsPadding()
    ) {
        HorizontalPager(
            state = state,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary)
        ) { page ->
            val p = if (page != state.pageCount - 1) pages[page] else null

            if (p == null) {
                EndPageUI(modifier = Modifier)
            } else
                PageUIBig(modifier = Modifier, page = p)
        }

        PageIndicator(
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
            numberOfPages = state.pageCount,
            selectedPage = state.currentPage,
            defaultRadius = 20.dp,
            selectedLength = 50.dp,
            space = 16.dp,
            animationDurationInMillis = 500,
        )
    }
}