package com.fresnohernandez99.stpt.room.converters

import com.fresnohernandez99.stpt.data.remote.model.DictionaryResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DictionaryResponseConverterTest {
    private val converter = DictionaryResponseConverter()

    @Test
    fun fromDictionaryResponse_null_returnsNull() {
        assertNull(converter.fromDictionaryResponse(null))
    }

    @Test
    fun toDictionaryResponse_null_returnsNull() {
        assertNull(converter.toDictionaryResponse(null))
        assertNull(converter.toDictionaryResponse(""))
    }

    @Test
    fun roundTrip_worksCorrectly() {
        val original = DictionaryResponse(word = "test", entries = emptyList())
        val json = converter.fromDictionaryResponse(original)
        val result = converter.toDictionaryResponse(json)
        
        assertNotNull(result)
        assertEquals(original.word, result.word)
    }
}
