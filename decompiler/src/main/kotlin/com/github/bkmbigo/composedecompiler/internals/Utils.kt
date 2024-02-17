package com.github.bkmbigo.composedecompiler.internals

var uniqueNumber = 0

// jetTestUtils
internal fun String.trimTrailingWhitespaces(): String =
    this.split('\n').joinToString(separator = "\n") { it.trimEnd() }

// jetTestUtils
internal fun String.trimTrailingWhitespacesAndAddNewlineAtEOF(): String =
    this.trimTrailingWhitespaces().let { result ->
        if (result.endsWith("\n")) result else result + "\n"
    }
