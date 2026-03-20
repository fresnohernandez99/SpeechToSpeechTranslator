package com.fresnohernandez99.stpt.domain.model

data class Language(
    val code: String,
    val name: String,
    val flag: String = ""
) {
    companion object {
        val Detect = Language("auto", "Detect Language", "🔍")
        val Spanish = Language("es", "Spanish", "🇪🇸")

        fun getFilteredLanguages(
            allLanguages: List<Language>,
            priorityLanguage: Language? = null,
            excludeLanguages: List<Language> = emptyList()
        ): List<Language> {
            val filtered = allLanguages.filter { it !in excludeLanguages }
            
            return if (priorityLanguage != null && filtered.contains(priorityLanguage)) {
                listOf(priorityLanguage) + (filtered - priorityLanguage)
            } else {
                filtered
            }
        }
    }
}
