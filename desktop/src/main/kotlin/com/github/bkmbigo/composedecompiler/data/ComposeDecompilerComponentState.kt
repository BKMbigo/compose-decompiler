package com.github.bkmbigo.composedecompiler.data

import com.github.bkmbigo.composedecompiler.DecompilerConfiguration

sealed class ComposeDecompilerComponentState(
    val currentPage: ComposeDecompilerPage
) {

    data class StabilityInferencer(
        val inferredStability: String? = null
    ) : ComposeDecompilerComponentState(currentPage = ComposeDecompilerPage.StabilityInferencer)

    data class IrDecompiler(
        val decompiledIr: String = ""
    ) : ComposeDecompilerComponentState(currentPage = ComposeDecompilerPage.IrDecompiler)

}

enum class ComposeDecompilerPage {
    StabilityInferencer,
    IrDecompiler
}

data class ComposeDecompilerBaseState(
    val decompilerRunState: DecompilerRunState = DecompilerRunState.Ready,
    val decompilerConfiguration: DecompilerConfiguration = DecompilerConfiguration(),
    val informationBannerState: InformationBannerState? = null
)

data class InformationBannerState(
    val informationType: InformationType,
    val mesage: String
)

enum class DecompilerRunState {
    Ready,
    Building
}

enum class InformationType {
    Info,
    Warning,
    Error
}

