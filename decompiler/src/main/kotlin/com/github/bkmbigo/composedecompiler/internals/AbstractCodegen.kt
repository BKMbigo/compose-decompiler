package com.github.bkmbigo.composedecompiler.internals

import com.github.bkmbigo.composedecompiler.DecompilerConfiguration
import com.github.bkmbigo.composedecompiler.internals.facade.SourceFile
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.codegen.GeneratedClassLoader
import java.io.File

internal abstract class AbstractCodegen(
    configuration: DecompilerConfiguration,
) : AbstractCompiler(
    configuration = configuration
) {

    protected fun classLoader(
        @Language("kotlin")
        source: String,
        fileName: String,
        dumpClasses: Boolean = false
    ): GeneratedClassLoader {
        val loader = createClassLoader(listOf(SourceFile(fileName, source)))
        if (dumpClasses) dumpClasses(loader)
        return loader
    }

    protected fun classLoader(
        sources: Map<String, String>,
        dumpClasses: Boolean = false
    ): GeneratedClassLoader {
        val loader = createClassLoader(
            sources.map { (fileName, source) -> SourceFile(fileName, source) }
        )
        if (dumpClasses) dumpClasses(loader)
        return loader
    }

    protected fun classLoader(
        platformSources: Map<String, String>,
        commonSources: Map<String, String>,
        dumpClasses: Boolean = false
    ): GeneratedClassLoader {
        val loader = createClassLoader(
            platformSources.map { (fileName, source) -> SourceFile(fileName, source) },
            commonSources.map { (fileName, source) -> SourceFile(fileName, source) }
        )
        if (dumpClasses) dumpClasses(loader)
        return loader
    }

    protected fun classLoader(
        sources: Map<String, String>,
        additionalPaths: List<File>,
        dumpClasses: Boolean = false
    ): GeneratedClassLoader {
        val loader = createClassLoader(
            sources.map { (fileName, source) -> SourceFile(fileName, source) },
            additionalPaths = additionalPaths
        )
        if (dumpClasses) dumpClasses(loader)
        return loader
    }

    protected fun testCompile(@Language("kotlin") source: String, dumpClasses: Boolean = false) {
        classLoader(source, "Test.kt", dumpClasses)
    }

    private fun dumpClasses(loader: GeneratedClassLoader) {
        for (
        file in loader.allGeneratedFiles.filter {
            it.relativePath.endsWith(".class")
        }
        ) {
            println("------\nFILE: ${file.relativePath}\n------")
            println(file.asText())
        }
    }

}
