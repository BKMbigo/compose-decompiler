import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.bkmbigo.composedecompiler.presentation.ComposeDecompilerTheme
import com.github.bkmbigo.composedecompiler.presentation.pages.HomeScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose Decompiler"
    ) {

        val initialDarkMode = isSystemInDarkTheme()

        var isDarkTheme by remember { mutableStateOf(initialDarkMode) }

        ComposeDecompilerTheme(
            isDarkMode = isDarkTheme
        ) {
            HomeScreen(
                toggleDarkMode = {
                    isDarkTheme = !isDarkTheme
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
