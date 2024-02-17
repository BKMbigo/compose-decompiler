package com.github.bkmbigo.composedecompiler.presentation.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.composedecompiler.data.ComposeDecompilerComponentState
import com.github.bkmbigo.composedecompiler.presentation.components.codeeditor.CodeEditor
import com.github.bkmbigo.composedecompiler.presentation.components.codeeditor.CodeEditorState
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.VerticalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun StabilityInferencerPage(
    componentState: ComposeDecompilerComponentState.StabilityInferencer,
    codeEditorState: CodeEditorState,
    modifier: Modifier = Modifier
) {

    val splitPaneState = rememberSplitPaneState(
        initialPositionPercentage = 0.8f
    )

    VerticalSplitPane(
        modifier = modifier,
        splitPaneState = splitPaneState
    ) {
        first {
            CodeEditor(
                codeEditorState,
                modifier = Modifier.fillMaxSize()
            )
        }

        second {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                componentState.inferredStability?.let {
                    Text(it)
                }
            }
        }
    }

}
