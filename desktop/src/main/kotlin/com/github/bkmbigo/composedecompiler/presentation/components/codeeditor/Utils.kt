package com.github.bkmbigo.composedecompiler.presentation.components.codeeditor

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.Theme
import java.io.IOException

internal fun RSyntaxTextArea.configureSyntaxTextArea(
    codeEditorLanguage: CodeEditorLanguage,
    isDarkMode: Boolean
) {

    syntaxEditingStyle = if (codeEditorLanguage == CodeEditorLanguage.Kotlin) {
        SyntaxConstants.SYNTAX_STYLE_KOTLIN
    } else {
        null
    }

    isCodeFoldingEnabled = true
    antiAliasingEnabled = true
    tabsEmulated = true
    tabSize = 4
    currentLineHighlightColor = java.awt.Color.decode("#F5F8FF")
    try {
        val theme = Theme.load(
            RSyntaxTextArea::class.java.getResourceAsStream(
                if (isDarkMode)
                    "/org/fife/ui/rsyntaxtextarea/themes/dark.xml"
                else
                    "/org/fife/ui/rsyntaxtextarea/themes/idea.xml"
            )
        )
        theme.apply(this)
    } catch (ioe: IOException) {
        ioe.printStackTrace()
    }

}
