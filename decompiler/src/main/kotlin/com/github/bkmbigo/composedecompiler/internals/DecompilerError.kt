package com.github.bkmbigo.composedecompiler.internals

internal class DecompilerError(val original: Throwable) : Exception(original)
