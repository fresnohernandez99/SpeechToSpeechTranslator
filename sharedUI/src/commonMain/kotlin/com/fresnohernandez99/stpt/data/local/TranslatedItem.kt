package com.fresnohernandez99.stpt.data.local

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fresnohernandez99.stpt.data.remote.model.DictionaryResponse

@Entity(tableName = "translated_item")
@Immutable
data class TranslatedItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalText: String,
    val translatedText: String,
    val originalLanguage: String,
    val translatedTo: String,
    val updateAt: Long,
    val dictionaryResponse: DictionaryResponse? = null
)
