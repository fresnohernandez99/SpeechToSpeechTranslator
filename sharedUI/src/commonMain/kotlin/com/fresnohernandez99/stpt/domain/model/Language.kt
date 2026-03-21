package com.fresnohernandez99.stpt.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Language(
    val code: String,
    val name: String,
    val flag: String = ""
) {
    companion object {
        val Detect = Language("auto", "Detect Language (beta)", "🔍")
        val Spanish = Language("es", "Spanish", "🇪🇸")

        val list: List<Language> = listOf(
            Language("en", "English", "🇺🇸"),
            Language("ar", "Arabic", "🇸🇦"),
            Language("ca", "Catalan", "🇪🇸"),
            Language("zh", "Chinese", "🇨🇳"),
            Language("cs", "Czech", "🇨🇿"),
            Language("nl", "Dutch", "🇳🇱"),
            Language("fi", "Finnish", "🇫🇮"),
            Language("fr", "French", "🇫🇷"),
            Language("gl", "Galician", "🇪🇸"),
            Language("de", "German", "🇩🇪"),
            Language("gu", "Gujarati", "🇮🇳"),
            Language("hi", "Hindi", "🇮🇳"),
            Language("id", "Indonesian", "🇮🇩"),
            Language("it", "Italian", "🇮🇹"),
            Language("ja", "Japanese", "🇯🇵"),
            Language("ko", "Korean", "🇰🇷"),
            Language("ms", "Malay", "🇲🇾"),
            Language("no", "Norwegian", "🇳🇴"),
            Language("sk", "Slovak", "🇸🇰"),
            Language("fa", "Persian (Farsi)", "🇮🇷"),
            Language("pl", "Polish", "🇵🇱"),
            Language("pt", "Portuguese", "🇧🇷"),
            Language("ru", "Russian", "🇷🇺"),
            Language.Spanish,
            Language("sv", "Swedish", "🇸🇪"),
            Language("tl", "Tagalog", "🇵🇭"),
            Language("th", "Thai", "🇹🇭"),
            Language("tr", "Turkish", "🇹🇷"),
            Language("uk", "Ukrainian", "🇺🇦"),
            Language("ur", "Urdu", "🇵🇰"),
            Language("vi", "Vietnamese", "🇻🇳")
        )

        fun getFilteredLanguages(
            allLanguages: List<Language>,
            priorityLanguage: Language,
            excludeLanguages: List<Language> = emptyList()
        ): List<Language> {
            val filtered = allLanguages.filter { it !in excludeLanguages }
            
            return listOf(priorityLanguage) + (filtered - priorityLanguage)
        }
    }
}
