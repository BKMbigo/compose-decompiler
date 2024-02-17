package com.github.bkmbigo.composedecompiler.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.lwjgl.util.tinyfd.TinyFileDialogs.tinyfd_selectFolderDialog

interface DirectoryPicker {

    suspend fun selectDirectory(
        initialDirectory: String? = null,
        title: String = "Compose Decompiler"
    ): String?

}

@Composable
fun rememberDirectoryPicker(): DirectoryPicker {
    return remember {
        object : DirectoryPicker {
            override suspend fun selectDirectory(initialDirectory: String?, title: String): String? {
                val initDir = initialDirectory ?: System.getProperty("user.dir")
                return chooseDirectory(initDir, title)
            }
        }
    }
}

private suspend fun chooseDirectory(
    initialDirectory: String,
    title: String
): String? = withContext(Dispatchers.IO) {
    tinyfd_selectFolderDialog(
        title,
        initialDirectory
    )
}
