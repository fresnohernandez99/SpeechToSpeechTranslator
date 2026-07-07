package com.fresnohernandez99.stpt.presentation.history

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.fresnohernandez99.stpt.data.local.TranslatedItem
import com.fresnohernandez99.stpt.presentation.components.AppScaffold
import com.fresnohernandez99.stpt.presentation.components.BackTopBar
import com.fresnohernandez99.stpt.presentation.history.components.TranslatedItemUi
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.presentation.navigation.LocalNavController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.history_title

@Composable
fun HistoryScreen(
    link: Destination.History,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val navHostController = LocalNavController.current

    val historyList = viewModel.history.collectAsLazyPagingItems()

    val selectedItem by viewModel.selectedItem.collectAsState()

    AppScaffold(
        topBar = {
            BackTopBar(
                title = stringResource(Res.string.history_title),
                onBack = { navHostController.navigateUp() },
                containerColor = MaterialTheme.colorScheme.primary
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
    ) { paddingValues ->
        HistoryContent(
            Modifier.consumeWindowInsets(paddingValues).padding(paddingValues),
            historyList = historyList,
            selectItem = viewModel::selectItem,
            selectedItem = selectedItem
        )
    }
}

@Composable
fun HistoryContent(
    modifier: Modifier = Modifier,
    historyList: LazyPagingItems<TranslatedItem>,
    selectItem: (TranslatedItem) -> Unit,
    selectedItem: TranslatedItem?
) {
    val isAppendLoading by remember(historyList) {
        derivedStateOf { historyList.loadState.append is LoadState.Loading }
    }

    LazyColumn(modifier = modifier) {
        items(
            count = historyList.itemCount,
            key = historyList.itemKey { h -> h.id },
            contentType = historyList.itemContentType { "h_row_type" }
        ) { index ->
            val h = historyList[index]
            if (h != null) {
                TranslatedItemUi(
                    Modifier, h,
                    isExpanded = selectedItem?.id == h.id,
                    onToggleExpand = {
                        selectItem(h)
                    }
                )
            }
        }

        if (isAppendLoading) {
            item(key = "loading_footer", contentType = "loading_type") {
                CircularProgressIndicator()
            }
        }
    }

}