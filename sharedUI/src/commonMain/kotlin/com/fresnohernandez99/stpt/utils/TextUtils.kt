package com.fresnohernandez99.stpt.utils

/**
 * Utility functions for text processing.
 */
/**
 * Checks if the given [text] is a single word and not a sentence.
 * A single word is defined as having no whitespace after trimming.
 */
fun String.isSingleWord(): Boolean {
    val trimmed = this.trim()
    if (trimmed.isEmpty()) return false

    // Check if there are any whitespace characters within the trimmed string
    return !trimmed.contains(Regex("\\s"))
}
