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
    val pronunciations: List<PronunciationDto> = emptyList(),
    val senses: List<SensesDto> = emptyList(),
    val synonyms: List<String> = emptyList(),
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
    val text: String
)

@Immutable
@Serializable
data class SensesDto(
    val definition: String = "",
    val examples: List<String> = emptyList(),
)
