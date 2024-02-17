package com.github.bkmbigo.composedecompiler.presentation.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.composedecompiler.data.*
import com.github.bkmbigo.composedecompiler.presentation.components.DecompilerSettingsDialog
import com.github.bkmbigo.composedecompiler.presentation.components.InformationBanner
import com.github.bkmbigo.composedecompiler.presentation.components.SidePanelMenu
import com.github.bkmbigo.composedecompiler.presentation.components.TopActionsRow
import com.github.bkmbigo.composedecompiler.presentation.components.codeeditor.CodeEditorState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    toggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val codeEditorState = remember { CodeEditorState() }
    val composeDecompilerPresenter = remember { CompilerDecompilerPresenter() }

    var showSettingsDialog by remember { mutableStateOf(false) }

    val componentState by composeDecompilerPresenter.componentState.collectAsState()
    val baseState by composeDecompilerPresenter.baseState.collectAsState()

    val currentPage by remember { derivedStateOf { componentState.currentPage } }

    Surface {
        Column(
            modifier = modifier
        ) {

            if (showSettingsDialog) {
                DecompilerSettingsDialog(
                    configuration = baseState.decompilerConfiguration,
                    onConfigurationChanged = {
                        composeDecompilerPresenter.changeConfiguration(it)
                        showSettingsDialog = false
                    },
                    onDismissRequest = {
                        showSettingsDialog = false
                    }
                )
            }

            TopActionsRow(
                baseState = baseState,
                onToggleDarkMode = toggleDarkMode,
                onCompile = {
                    coroutineScope.launch {
                        composeDecompilerPresenter.compile(codeEditorState.code)
                    }
                },
                onOpenSettings = {
                    showSettingsDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .weight(1f, true)
                    .fillMaxWidth()
            ) {

                SidePanelMenu(
                    currentDestination = currentPage,
                    onDestinationChanged = {
                        composeDecompilerPresenter.changePage(it)
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                )

                Spacer(modifier = Modifier.width(4.dp))

                Column(
                    modifier = Modifier
                        .weight(1f, true)
                        .fillMaxHeight()
                ) {

                    baseState.informationBannerState?.let { infoState ->
                        InformationBanner(
                            informationBannerState = infoState,
                            onDismiss = {
                                composeDecompilerPresenter.closeInformationBanner()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    when (currentPage) {
                        ComposeDecompilerPage.StabilityInferencer -> {
                            StabilityInferencerPage(
                                componentState as ComposeDecompilerComponentState.StabilityInferencer,
                                codeEditorState = codeEditorState,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        ComposeDecompilerPage.IrDecompiler -> {
                            IrDecompilerPage(
                                componentState = componentState as ComposeDecompilerComponentState.IrDecompiler,
                                codeEditorState = codeEditorState,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }

}
