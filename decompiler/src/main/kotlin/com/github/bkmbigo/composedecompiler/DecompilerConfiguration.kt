package com.github.bkmbigo.composedecompiler

import androidx.compose.runtime.Stable

/**
 * Configuration for a Decompiler Instance
 * */
@Stable
data class DecompilerConfiguration(
    val ideaHome: String = "",

    // Compose Compiler Configuration
    val useFir: Boolean = false,
    val enableStringSkipping: Boolean = false,
    val enableIntrinsicRemember: Boolean = false,
    val enableTraceMarkers: Boolean = false,
    val enableLiveLiteralsV2: Boolean = false,

    // Decompiler Configuration
    val simplifyComposeIr: Boolean = true
)
