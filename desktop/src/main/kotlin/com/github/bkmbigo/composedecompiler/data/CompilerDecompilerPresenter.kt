package com.github.bkmbigo.composedecompiler.data

import com.github.bkmbigo.composedecompiler.Decompiler
import com.github.bkmbigo.composedecompiler.Decompiler.decompileToIr
import com.github.bkmbigo.composedecompiler.Decompiler.inferClassStability
import com.github.bkmbigo.composedecompiler.DecompilerConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.logging.Level
import java.util.logging.Logger

class CompilerDecompilerPresenter(
    private val decompiler: Decompiler = Decompiler
) {

    private val _componentState =
        MutableStateFlow<ComposeDecompilerComponentState>(ComposeDecompilerComponentState.StabilityInferencer())
    val componentState = _componentState.asStateFlow()

    private val _baseState = MutableStateFlow(ComposeDecompilerBaseState())
    val baseState = _baseState.asStateFlow()

    fun changePage(page: ComposeDecompilerPage) = when (page) {
        ComposeDecompilerPage.StabilityInferencer -> {
            _componentState.value = ComposeDecompilerComponentState.StabilityInferencer()
        }

        ComposeDecompilerPage.IrDecompiler -> {
            _componentState.value = ComposeDecompilerComponentState.IrDecompiler()
        }
    }

    fun changeConfiguration(decompilerConfiguration: DecompilerConfiguration) {
        _baseState.value = _baseState.value.copy(decompilerConfiguration = decompilerConfiguration)
    }

    fun closeInformationBanner() {
        _baseState.value = _baseState.value.copy(informationBannerState = null)
    }

    /* Performs actions specified */
    suspend fun compile(
        code: String
    ) {
        withContext(Dispatchers.IO) {
            _baseState.value = _baseState.value.copy(
                decompilerRunState = DecompilerRunState.Building,
                informationBannerState = null
            )

            when (_componentState.value) {
                is ComposeDecompilerComponentState.StabilityInferencer -> {
                    try {
                        _componentState.value = ComposeDecompilerComponentState.StabilityInferencer(
                            inferredStability = decompiler.inferClassStability(
                                code,
                                configuration = _baseState.value.decompilerConfiguration
                            )
                        )

                        _baseState.value = _baseState.value.copy(decompilerRunState = DecompilerRunState.Ready)
                    } catch (e: Exception) {
                        Logger.getAnonymousLogger().log(Level.INFO, e.message)

                        _baseState.value = _baseState.value.copy(
                            decompilerRunState = DecompilerRunState.Ready,
                            informationBannerState = InformationBannerState(
                                informationType = InformationType.Error,
                                "Error: ${e.message}"
                            )
                        )
                    }
                }

                is ComposeDecompilerComponentState.IrDecompiler -> {
                    try {
                        _componentState.value = ComposeDecompilerComponentState.IrDecompiler(
                            decompiledIr = Decompiler.decompileToIr(
                                code = code,
                                configuration = _baseState.value.decompilerConfiguration
                            )
                        )

                        _baseState.value = _baseState.value.copy(decompilerRunState = DecompilerRunState.Ready)
                    } catch (e: Exception) {
                        Logger.getAnonymousLogger().log(Level.INFO, e.message)

                        _baseState.value = _baseState.value.copy(
                            decompilerRunState = DecompilerRunState.Ready,
                            informationBannerState = InformationBannerState(
                                informationType = InformationType.Error,
                                "Error: ${e.message}"
                            )
                        )
                    }
                }
            }
        }
    }

}
