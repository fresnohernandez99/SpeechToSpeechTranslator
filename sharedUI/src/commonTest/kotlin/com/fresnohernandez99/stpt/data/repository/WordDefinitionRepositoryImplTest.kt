package com.fresnohernandez99.stpt.data.repository

import com.fresnohernandez99.stpt.data.remote.DictionaryApiService
import com.fresnohernandez99.stpt.data.remote.model.DictionaryResponse
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class WordDefinitionRepositoryImplTest {

    private class FakeApiService : DictionaryApiService(HttpClient()) {
        var shouldFail = false
        override suspend fun getWordDefinition(languageCode: String, word: String): DictionaryResponse {
            if (shouldFail) throw Exception("API Error")
            return DictionaryResponse(word = word, entries = emptyList())
        }
    }

    @Test
    fun getWordDefinition_success_returnsSuccessResult() = runTest {
        val fakeApi = FakeApiService()
        val repository = WordDefinitionRepositoryImpl(fakeApi)
        
        val result = repository.getWordDefinition("en", "hello")
        
        assertTrue(result.isSuccess)
    }

    @Test
    fun getWordDefinition_error_returnsFailureResult() = runTest {
        val fakeApi = FakeApiService()
        fakeApi.shouldFail = true
        val repository = WordDefinitionRepositoryImpl(fakeApi)
        
        val result = repository.getWordDefinition("en", "hello")
        
        assertTrue(result.isFailure)
    }
}
