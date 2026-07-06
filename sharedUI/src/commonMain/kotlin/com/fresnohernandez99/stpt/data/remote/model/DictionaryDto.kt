package com.fresnohernandez99.stpt.data.remote.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class DictionaryResponse(
    val word: String,
    val entries: List<EntryDto>
)

@Immutable
@Serializable
data class EntryDto(
    val language: LanguageInfoDto,
    val partOfSpeech: String? = null,
    val pronunciations: List<PronunciationDto> = emptyList(),
    val meanings: List<MeaningDto> = emptyList()
)

@Immutable
@Serializable
data class LanguageInfoDto(
    val code: String,
    val name: String
)

@Serializable
data class PronunciationDto(
    val type: String,
    val value: String
)

@Immutable
@Serializable
data class MeaningDto(
    val glosses: List<String> = emptyList(),
    val examples: List<String> = emptyList(),
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val translations: List<TranslationDto> = emptyList()
)

@Immutable
@Serializable
data class TranslationDto(
    val language: LanguageInfoDto,
    val value: String
)
