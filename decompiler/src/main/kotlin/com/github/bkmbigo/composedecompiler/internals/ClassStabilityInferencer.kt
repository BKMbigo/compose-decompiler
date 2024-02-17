package com.github.bkmbigo.composedecompiler.internals

import androidx.compose.compiler.plugins.kotlin.analysis.FqNameMatcher
import androidx.compose.compiler.plugins.kotlin.analysis.Stability
import androidx.compose.compiler.plugins.kotlin.analysis.StabilityInferencer
import com.github.bkmbigo.composedecompiler.DecompilerConfiguration
import com.github.bkmbigo.composedecompiler.internals.facade.SourceFile
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.statements

internal class ClassStabilityInferencer(
    configuration: DecompilerConfiguration
) : AbstractIrTransform(configuration) {

    fun inferStability(
        @Language("kotlin")
        classDefSrc: String,
        externalTypes: Set<String> = emptySet(),
    ): Stability {
        val source = """
            import androidx.compose.runtime.mutableStateOf
            import androidx.compose.runtime.getValue
            import androidx.compose.runtime.setValue
            import androidx.compose.runtime.Composable
            import androidx.compose.runtime.Stable
            import androidx.compose.runtime.State
            import kotlin.reflect.KProperty

            $classDefSrc

            class Unstable { var value: Int = 0 }
        """.trimIndent()

        val files = listOf(SourceFile("Test.kt", source))
        val irModule = compileToIr(files)
        val irClass = irModule.files.last().declarations.first() as IrClass
        val externalTypeMatchers = externalTypes.map { FqNameMatcher(it) }.toSet()
        val stabilityInferencer = StabilityInferencer(irModule.descriptor, externalTypeMatchers)
        val classStability = stabilityInferencer.stabilityOf(irClass.defaultType as IrType)

        return classStability
    }

    private fun inferStability(
        externalSrc: String,
        localSrc: String,
        expression: String,
        dumpClasses: Boolean = false,
        externalTypes: Set<String> = emptySet()
    ): String {
        val irModule = buildModule(
            externalSrc,
            """
                $localSrc

                fun TestFunction() = $expression
            """.trimIndent(),
            dumpClasses
        )
        val irTestFn = irModule
            .files
            .last()
            .declarations
            .filterIsInstance<IrSimpleFunction>()
            .first { it.name.asString() == "TestFunction" }

        val irExpr = when (val lastStatement = irTestFn.body!!.statements.last()) {
            is IrReturn -> lastStatement.value
            is IrExpression -> lastStatement
            else -> error("unexpected statement: $lastStatement")
        }

        val externalTypeMatchers = externalTypes.map { FqNameMatcher(it) }.toSet()
        val exprStability =
            StabilityInferencer(
                currentModule = irModule.descriptor,
                externalStableTypeMatchers = externalTypeMatchers
            ).stabilityOf(irExpr)

        return exprStability.toString()
    }

    private fun buildModule(
        @Language("kotlin")
        externalSrc: String,
        @Language("kotlin")
        localSrc: String,
        dumpClasses: Boolean = false,
        packageName: String = "dependency"
    ): IrModuleFragment {
        val dependencyFileName = "Test_REPLACEME_${uniqueNumber++}"

        val dependencySrc = """
            package $packageName
            
            import androidx.compose.runtime.mutableStateOf
            import androidx.compose.runtime.getValue
            import androidx.compose.runtime.setValue
            import androidx.compose.runtime.Composable
            import androidx.compose.runtime.Stable
            import androidx.compose.runtime.State
            import kotlin.reflect.KProperty
            
            class UnusedClass
            
            $externalSrc
        """.trimIndent()

        classLoader(dependencySrc, dependencyFileName, dumpClasses)
            .allGeneratedFiles
            .also {
                // Write the files to the class directory so they can be used by the next module
                // and the application
                it.writeToDir(classesDirectory)
            }

        val source = """
            import $packageName.*
            import androidx.compose.runtime.mutableStateOf
            import androidx.compose.runtime.getValue
            import androidx.compose.runtime.setValue
            import androidx.compose.runtime.Composable
            import androidx.compose.runtime.Stable
            import androidx.compose.runtime.State
            import kotlin.reflect.KProperty

            $localSrc
        """.trimIndent()

        val files = listOf(SourceFile("Decompiler.kt", source))
        return compileToIr(files, listOf(classesDirectory))

    }
}
