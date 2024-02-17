package com.github.bkmbigo.composedecompiler.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import com.github.bkmbigo.composedecompiler.DecompilerConfiguration
import kotlinx.coroutines.launch

@Composable
fun DecompilerSettingsDialog(
    configuration: DecompilerConfiguration,
    onConfigurationChanged: (DecompilerConfiguration) -> Unit,
    onDismissRequest: () -> Unit
) {

    // This will change when configuration is changed
    var temporaryIdeaHome by rememberSaveable(key = configuration.ideaHome, stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(configuration.ideaHome))
    }

    // Compose Compiler Options
    var isUseFir by remember(configuration.useFir) {
        mutableStateOf(configuration.useFir)
    }
    var isStrongSkippingEnabled by remember(configuration.enableStringSkipping) {
        mutableStateOf(configuration.enableStringSkipping)
    }
    var isLiveLiteralsV2Enabled by remember(configuration.enableLiveLiteralsV2) {
        mutableStateOf(configuration.enableLiveLiteralsV2)
    }
    var isIntrinsicRememberEnabled by remember(configuration.enableIntrinsicRemember) {
        mutableStateOf(configuration.enableIntrinsicRemember)
    }
    var isTraceMarkersEnabled by remember(configuration.enableTraceMarkers) {
        mutableStateOf(configuration.enableTraceMarkers)
    }

    // Ir Decompiler Options
    var isSimplifyingComposeIr by remember(configuration.simplifyComposeIr) {
        mutableStateOf(configuration.simplifyComposeIr)
    }


    DialogWindow(
        onCloseRequest = {
            onDismissRequest()
        },
        state = rememberDialogState(
            width = 600.dp,
            height = 800.dp
        ),
        title = "Decompiler Settings"
    ) {

        val verticalScrollState = rememberScrollState()

        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f, true)
                        .padding(horizontal = 4.dp)
                        .verticalScroll(state = verticalScrollState)
                ) {

                    IdeaHomeSetting(
                        ideaHomeValue = temporaryIdeaHome,
                        onIdeaHomeValue = {
                            temporaryIdeaHome = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ComposeCompilerSettings(
                        isUseFir = isUseFir,
                        setUseFir = {
                            isUseFir = it
                        },
                        isStrongSkippingEnabled = isStrongSkippingEnabled,
                        setStrongSkippingEnabled = {
                            isStrongSkippingEnabled = it
                        },
                        isLiveLiteralsV2Enabled = isLiveLiteralsV2Enabled,
                        setLiveLiteralsV2Enabled = {
                            isLiveLiteralsV2Enabled = it
                        },
                        isIntrinsicRememberEnabled = isIntrinsicRememberEnabled,
                        setIntrinsicRememberEnabled = {
                            isIntrinsicRememberEnabled = it
                        },
                        isTraceMarkersEnabled = isTraceMarkersEnabled,
                        setTraceMarkersEnabled = {
                            isTraceMarkersEnabled = it
                        },
                        modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    IrDecompilerSettings(
                        isSimplifyComposeIr = isSimplifyingComposeIr,
                        setSimplifyComposeIr = {
                            isSimplifyingComposeIr = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onConfigurationChanged(
                                DecompilerConfiguration(
                                    ideaHome = temporaryIdeaHome.text,
                                    useFir = isUseFir,
                                    enableStringSkipping = isStrongSkippingEnabled,
                                    enableIntrinsicRemember = isIntrinsicRememberEnabled,
                                    enableTraceMarkers = isTraceMarkersEnabled,
                                    enableLiveLiteralsV2 = isLiveLiteralsV2Enabled,
                                    simplifyComposeIr = isSimplifyingComposeIr
                                )
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text("Save Settings")
                    }
                }
            }
        }
    }
}

@Composable
private fun IdeaHomeSetting(
    ideaHomeValue: TextFieldValue,
    onIdeaHomeValue: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val directoryPicker = rememberDirectoryPicker()
    var isSelectingDirectory by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {

        SectionTitle(
            title = "IDEA Settings",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Specify a directory where an IntelliJ-Based IDE is located (You can use the directory for Android Studio). If null, it defaults to System Property 'idea.home'",
            fontSize = 10.sp,
            color = LocalContentColor.current.copy(alpha = 0.5f),
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Ideally, the file `%idea.home\\bin\\idea.properties` should exist.",
            fontSize = 10.sp,
            color = LocalContentColor.current.copy(alpha = 0.5f),
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = ideaHomeValue,
            onValueChange = {
                if (!isSelectingDirectory) {
                    onIdeaHomeValue(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 4.dp),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 14.sp
            ),
            label = {
                Text("IDEA Home")
            },
            placeholder = {
                Text("idea.home")
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            if (!isSelectingDirectory) {
                                isSelectingDirectory = true
                                directoryPicker.selectDirectory(
                                    initialDirectory = ideaHomeValue.text.ifBlank { null },
                                    title = "IDEA Directory Picker"
                                )?.let {
                                    onIdeaHomeValue(TextFieldValue(it))
                                    isSelectingDirectory = false
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Default)
                ) {
                    Icon(
                        imageVector = if (!isSelectingDirectory)
                            Icons.Default.FolderOpen
                        else
                            Icons.Default.Pending,
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Composable
private fun ComposeCompilerSettings(
    isUseFir: Boolean,
    setUseFir: (Boolean) -> Unit,
    isStrongSkippingEnabled: Boolean,
    setStrongSkippingEnabled: (Boolean) -> Unit,
    isLiveLiteralsV2Enabled: Boolean,
    setLiveLiteralsV2Enabled: (Boolean) -> Unit,
    isIntrinsicRememberEnabled: Boolean,
    setIntrinsicRememberEnabled: (Boolean) -> Unit,
    isTraceMarkersEnabled: Boolean,
    setTraceMarkersEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {

        SectionTitle(
            title = "Compose Settings",
            modifier = Modifier
                .fillMaxWidth()
        )

        CheckboxRowItem(
            title = "Use K2",
            isChecked = isUseFir,
            onCheckedChanged = setUseFir,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        CheckboxRowItem(
            title = "Enable Strong Skipping",
            isChecked = isStrongSkippingEnabled,
            onCheckedChanged = setStrongSkippingEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        CheckboxRowItem(
            title = "Enable Tracing Markers",
            isChecked = isTraceMarkersEnabled,
            onCheckedChanged = setTraceMarkersEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        CheckboxRowItem(
            title = "Enable Intrinsic Remember",
            isChecked = isIntrinsicRememberEnabled,
            onCheckedChanged = setIntrinsicRememberEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        CheckboxRowItem(
            title = "Enable Live Literals V2",
            isChecked = isLiveLiteralsV2Enabled,
            onCheckedChanged = setLiveLiteralsV2Enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

    }
}

@Composable
private fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        val colorScheme = MaterialTheme.colorScheme

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 4.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f, true)
                .fillMaxHeight()
                .padding(horizontal = 4.dp)
                .drawBehind {
                    drawLine(
                        color = colorScheme.onSurfaceVariant,
                        start = Offset(x = 0f, y = size.height / 2),
                        end = Offset(x = size.width, y = size.height / 2)
                    )
                }
        )
    }

}

@Composable
private fun IrDecompilerSettings(
    isSimplifyComposeIr: Boolean,
    setSimplifyComposeIr: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SectionTitle(
            title = "Compose Decompiler Settings",
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        CheckboxRowItem(
            title = "Simplify Compose Ir",
            isChecked = isSimplifyComposeIr,
            onCheckedChanged = setSimplifyComposeIr,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CheckboxRowItem(
    title: String,
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title
        )

        // This Spacer adds space between the elements in-case one composable takes the full width
        Spacer(modifier = Modifier.width(4.dp))

        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChanged,
            modifier = Modifier
                .padding(vertical = 4.dp)
        )

    }
}
