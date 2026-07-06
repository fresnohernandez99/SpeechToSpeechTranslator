package com.fresnohernandez99.stpt.data.repository

import com.fresnohernandez99.stpt.data.remote.DictionaryApiService
import com.fresnohernandez99.stpt.data.remote.model.DictionaryResponse
import com.fresnohernandez99.stpt.domain.repository.WordDefinitionRepository

class WordDefinitionRepositoryImpl(
    private val apiService: DictionaryApiService
) : WordDefinitionRepository {
    override suspend fun getWordDefinition(languageCode: String, word: String): Result<DictionaryResponse> {
        return runCatching {
            apiService.getWordDefinition(languageCode, word)
        }
    }
}
