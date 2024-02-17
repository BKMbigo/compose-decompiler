package com.github.bkmbigo.composedecompiler.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun ComposeDecompilerTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = if (isDarkMode)
            darkColorScheme()
        else
            lightColorScheme()
    ) {

        CompositionLocalProvider(
            LocalDarkTheme provides isDarkMode
        ) {
            content()
        }
    }
}

val LocalDarkTheme = staticCompositionLocalOf { false }
