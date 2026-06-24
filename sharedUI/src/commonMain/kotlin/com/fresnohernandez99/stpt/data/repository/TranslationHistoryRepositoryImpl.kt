package com.fresnohernandez99.stpt.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fresnohernandez99.stpt.data.local.TranslatedItem
import com.fresnohernandez99.stpt.domain.repository.TranslationHistoryRepository
import com.fresnohernandez99.stpt.room.TranslatedItemDao
import kotlinx.coroutines.flow.Flow

class TranslationHistoryRepositoryImpl(
    private val dao: TranslatedItemDao
) : TranslationHistoryRepository {

    override fun getTranslationHistory(): Flow<PagingData<TranslatedItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getAll() }
        ).flow
    }

    override suspend fun addTranslation(item: TranslatedItem) {
        dao.insert(item)
    }

    override suspend fun deleteTranslation(item: TranslatedItem) {
        dao.delete(item)
    }

    override suspend fun getLast3(): List<TranslatedItem> {
        return dao.getLast3()
    }
}
