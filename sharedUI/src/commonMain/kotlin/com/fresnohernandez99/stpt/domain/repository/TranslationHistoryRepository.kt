package com.fresnohernandez99.stpt.domain.repository

import androidx.paging.PagingData
import com.fresnohernandez99.stpt.data.local.TranslatedItem
import kotlinx.coroutines.flow.Flow

interface TranslationHistoryRepository {
    fun getTranslationHistory(): Flow<PagingData<TranslatedItem>>
    suspend fun addTranslation(item: TranslatedItem)
    suspend fun deleteTranslation(item: TranslatedItem)
    suspend fun getLast3(): List<TranslatedItem>
}
