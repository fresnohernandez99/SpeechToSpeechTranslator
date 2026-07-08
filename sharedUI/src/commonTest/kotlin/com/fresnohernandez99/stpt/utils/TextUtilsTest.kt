package com.fresnohernandez99.stpt.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TextUtilsTest {

    @Test
    fun isSingleWord_withSingleWord_returnsTrue() {
        assertTrue("Hello".isSingleWord(), "Word 'Hello' should be single word")
        assertTrue("  World  ".isSingleWord(), "Trimmed 'World' should be single word")
        assertTrue("KMP".isSingleWord(), "Word 'KMP' should be single word")
    }

    @Test
    fun isSingleWord_withMultipleWords_returnsFalse() {
        assertFalse("Hello World".isSingleWord(), "'Hello World' should NOT be single word")
        assertFalse("This is a sentence".isSingleWord(), "Sentence should NOT be single word")
    }

    @Test
    fun isSingleWord_withSentence_returnsFalse() {
        assertFalse("Hello how are you".isSingleWord(), "Sentence should NOT be single word")
    }

    @Test
    fun isSingleWord_withEmptyString_returnsFalse() {
        assertFalse("".isSingleWord(), "Empty string should NOT be single word")
        assertFalse("   ".isSingleWord(), "Blank string should NOT be single word")
    }
}
