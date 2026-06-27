package com.fresnohernandez99.stpt.utils

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class Gson {
    var json: Json = initialize()

    @OptIn(InternalSerializationApi::class)
    fun <T : Any> toJson(data: T?, serializerAux: SerializationStrategy<T>? = null): String {
        // Usar el serializador correspondiente a la clase de data

        if (data == null) return ""

        val serializer = serializerAux ?: data::class.serializer() as KSerializer<T>
        return json.encodeToString(serializer, data)
    }

    @OptIn(InternalSerializationApi::class)
    @Throws
    fun <T : Any> fromJson(
        jsonString: String,
        clazz: KClass<T>,
        serializerAux: DeserializationStrategy<T>? = null
    ): T {
        val serializer = serializerAux ?: clazz.serializer()
        return json.decodeFromString(serializer, jsonString)
    }

    companion object {
        fun initialize(): Json {
            return Json { encodeDefaults = true; ignoreUnknownKeys = true }
        }
    }

    // EXAMPLE:
    // gson.toJson(category, Category.serializer())
    // gson.fromJson(str, Category::class, Category.serializer())
}