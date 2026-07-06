package com.fresnohernandez99.stpt.data.remote

import com.fresnohernandez99.stpt.data.remote.model.DictionaryResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class DictionaryApiService(
    private val client: HttpClient,
    private val baseUrl: String = "https://freedictionaryapi.com/api/v1"
) {
    suspend fun getWordDefinition(languageCode: String, word: String): DictionaryResponse {
        return client.get("$baseUrl/entries/$languageCode/$word").body()
    }
}
