package com.github.bkmbigo.composedecompiler

import com.github.bkmbigo.composedecompiler.internals.AbstractIrTransform
import com.github.bkmbigo.composedecompiler.internals.ClassStabilityInferencer
import org.intellij.lang.annotations.Language

/*
* This serves as the main and only entry point to the decompiler
* */
object Decompiler {

    fun Decompiler.inferClassStability(
        @Language("Kotlin")
        code: String,
        configuration: DecompilerConfiguration
    ): String = ClassStabilityInferencer(configuration).inferStability(code).toString()


    fun Decompiler.decompileToIr(
        @Language("Kotlin")
        code: String,
        configuration: DecompilerConfiguration
    ): String = AbstractIrTransform(configuration).transform(source = code)


}
