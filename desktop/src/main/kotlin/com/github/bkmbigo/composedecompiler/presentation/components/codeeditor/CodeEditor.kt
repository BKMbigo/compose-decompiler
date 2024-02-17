package com.github.bkmbigo.composedecompiler.presentation.components.codeeditor

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import com.github.bkmbigo.composedecompiler.presentation.LocalDarkTheme
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import javax.swing.SwingUtilities
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/* Note: [onCodeChanged] should not cause recomposition */
@Composable
fun CodeEditor(
    state: CodeEditorState,
    language: CodeEditorLanguage = CodeEditorLanguage.Kotlin,
    modifier: Modifier = Modifier
) {

    val colorScheme = MaterialTheme.colorScheme
    val isDarkMode = LocalDarkTheme.current

    val sourceTextArea = remember {
        RSyntaxTextArea().apply {
            background = colorScheme.background.run {
                java.awt.Color(
                    red,
                    green,
                    blue
                )
            }

            configureSyntaxTextArea(language, isDarkMode)
            SwingUtilities.invokeLater { requestFocusInWindow() }
            document.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) {
                    state.onCodeChange(text)
                }

                override fun removeUpdate(p0: DocumentEvent?) {
                    state.onCodeChange(text)
                }

                override fun changedUpdate(p0: DocumentEvent?) {
                    state.onCodeChange(text)
                }
            })
        }
    }

    SwingPanel(
        modifier = modifier,
        background = MaterialTheme.colorScheme.background,
        factory = {
            RTextScrollPane(sourceTextArea)
        },
        update = {
            // This should not cause recomposition
            sourceTextArea.text = state.code
            sourceTextArea.configureSyntaxTextArea(language, isDarkMode)
        }
    )

}
