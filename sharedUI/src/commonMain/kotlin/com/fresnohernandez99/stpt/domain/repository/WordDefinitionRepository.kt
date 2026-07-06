package com.fresnohernandez99.stpt.domain.repository

import com.fresnohernandez99.stpt.data.remote.model.DictionaryResponse

interface WordDefinitionRepository {
    suspend fun getWordDefinition(languageCode: String, word: String): Result<DictionaryResponse>
}
