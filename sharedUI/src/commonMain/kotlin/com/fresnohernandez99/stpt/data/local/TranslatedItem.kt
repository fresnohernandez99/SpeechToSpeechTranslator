package com.fresnohernandez99.stpt.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translated_item")
data class TranslatedItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalText: String,
    val translatedText: String,
    val originalLanguage: String,
    val translatedTo: String,
    val updateAt: Long
)
