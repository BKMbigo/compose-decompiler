package com.github.bkmbigo.composedecompiler.presentation.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.composedecompiler.data.ComposeDecompilerComponentState
import com.github.bkmbigo.composedecompiler.presentation.components.codeeditor.CodeEditor
import com.github.bkmbigo.composedecompiler.presentation.components.codeeditor.CodeEditorState
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import org.jetbrains.skiko.Cursor

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun IrDecompilerPage(
    componentState: ComposeDecompilerComponentState.IrDecompiler,
    codeEditorState: CodeEditorState,
    modifier: Modifier = Modifier
) {
    val splitPaneState = rememberSplitPaneState(
        initialPositionPercentage = 0.45f
    )

    HorizontalSplitPane(
        splitPaneState = splitPaneState,
        modifier = modifier,
    ) {
        first {
            CodeEditor(
                codeEditorState,
                modifier = Modifier.fillMaxSize()
            )
        }

        splitter {
            visiblePart {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )
            }

            handle {
                Box(
                    modifier = Modifier
                        .markAsHandle()
                        .cursorForHorizontalResize()
                        .width(9.dp)
                        .fillMaxHeight()
                )
            }
        }

        second {
            CodeEditor(
                state = CodeEditorState(code = componentState.decompiledIr),
                modifier = Modifier.fillMaxSize()
            )
        }

    }


}

internal fun Modifier.cursorForHorizontalResize(): Modifier =
    pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))
