package com.github.bkmbigo.composedecompiler.internals

import androidx.compose.compiler.plugins.kotlin.ComposeConfiguration
import androidx.compose.compiler.plugins.kotlin.ComposePluginRegistrar
import com.github.bkmbigo.composedecompiler.DecompilerConfiguration
import com.github.bkmbigo.composedecompiler.internals.facade.KotlinCompilerFacade
import com.github.bkmbigo.composedecompiler.internals.facade.SourceFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.io.FileUtil
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.output.OutputFile
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.cli.jvm.config.configureJdkClasspathRoots
import org.jetbrains.kotlin.codegen.GeneratedClassLoader
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import java.io.File
import java.net.URLClassLoader

internal abstract class AbstractCompiler(
    val configuration: DecompilerConfiguration
) {

    private val decompilerRootDisposable = Disposer.newDisposable()

    init {
        System.setProperty("idea.home", configuration.ideaHome)
    }

    val defaultClassPath by lazy {
        System.getProperty("java.class.path")!!.split(
            System.getProperty("path.separator")
        ).map { File(it) }
    }

    val defaultClassPathRoots by lazy {
        defaultClassPath.filter {
            !it.path.contains("robolectric") && it.extension != "xml"
        }.toList()
    }

    companion object;

    protected open fun dispose() {
        Disposer.dispose(decompilerRootDisposable)
    }

    protected open fun CompilerConfiguration.updateConfiguration() {}

    fun CompilerConfiguration.setDecompilerConfiguration(configuration: DecompilerConfiguration) {
        put(ComposeConfiguration.STRONG_SKIPPING_ENABLED_KEY, configuration.enableStringSkipping)
        put(ComposeConfiguration.TRACE_MARKERS_ENABLED_KEY, configuration.enableTraceMarkers)
        put(ComposeConfiguration.INTRINSIC_REMEMBER_OPTIMIZATION_ENABLED_KEY, configuration.enableIntrinsicRemember)
        put(ComposeConfiguration.LIVE_LITERALS_V2_ENABLED_KEY, configuration.enableLiveLiteralsV2)
    }

    private fun createCompilerFacade(
        additionalPaths: List<File> = listOf(),
        registerExtensions: (Project.(CompilerConfiguration) -> Unit)? = null
    ) = KotlinCompilerFacade.create(
        decompilerRootDisposable,
        updateConfiguration = {
            val enableFir = configuration.useFir
            val languageVersion =
                if (enableFir)
                    LanguageVersion.KOTLIN_2_0
                else
                    LanguageVersion.KOTLIN_1_9

            val analysisFlag: Map<AnalysisFlag<*>, Any?> = mapOf(
                AnalysisFlags.allowUnstableDependencies to true,
                AnalysisFlags.skipPrereleaseCheck to true
            )

            languageVersionSettings = LanguageVersionSettingsImpl(
                languageVersion,
                ApiVersion.createByLanguageVersion(languageVersion),
                analysisFlag
            )

            updateConfiguration()
            setDecompilerConfiguration(configuration)
            addJvmClasspathRoots(additionalPaths)
            addJvmClasspathRoots(defaultClassPathRoots)

            if (!getBoolean(JVMConfigurationKeys.NO_JDK) && get(JVMConfigurationKeys.JDK_HOME) == null) {
                // We need to set `JDK_HOME` explicitly to use JDK 17
                put(JVMConfigurationKeys.JDK_HOME, File(System.getProperty("java.home")!!))
            }
            configureJdkClasspathRoots()
        },
        registerExtensions = registerExtensions ?: { configuration ->
            ComposePluginRegistrar.registerCommonExtensions(this)
            IrGenerationExtension.registerExtension(
                this,
                ComposePluginRegistrar.createComposeIrExtension(configuration)
            )
        }
    )

    protected fun compileToIr(
        sourceFiles: List<SourceFile>,
        additionalPaths: List<File> = listOf(),
        registerExtensions: (Project.(CompilerConfiguration) -> Unit)? = null
    ): IrModuleFragment = createCompilerFacade(additionalPaths, registerExtensions)
        .compileToIr(sourceFiles)


    protected fun createClassLoader(
        platformSourceFiles: List<SourceFile>,
        commonSourceFiles: List<SourceFile> = listOf(),
        additionalPaths: List<File> = listOf()
    ): GeneratedClassLoader {
        val classLoader = URLClassLoader(
            (additionalPaths + defaultClassPath).map {
                it.toURI().toURL()
            }.toTypedArray(),
            null
        )
        return GeneratedClassLoader(
            createCompilerFacade(additionalPaths)
                .compile(platformSourceFiles, commonSourceFiles).factory,
            classLoader
        )
    }

}

internal fun OutputFile.writeToDir(directory: File) =
    FileUtil.writeToFile(File(directory, relativePath), asByteArray())

internal fun Collection<OutputFile>.writeToDir(directory: File) = forEach { it.writeToDir(directory) }
