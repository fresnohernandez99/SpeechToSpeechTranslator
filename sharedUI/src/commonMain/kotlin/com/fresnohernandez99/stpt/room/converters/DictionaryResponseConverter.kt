package com.fresnohernandez99.stpt.room.converters

import androidx.room.TypeConverter
import com.fresnohernandez99.stpt.data.remote.model.DictionaryResponse
import com.fresnohernandez99.stpt.utils.Gson

class DictionaryResponseConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromDictionaryResponse(value: DictionaryResponse?): String? {
        if (value == null) return null
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDictionaryResponse(value: String?): DictionaryResponse? {
        if (value.isNullOrBlank()) return null
        return try {
            gson.fromJson(value, DictionaryResponse::class)
        } catch (e: Exception) {
            null
        }
    }
}
