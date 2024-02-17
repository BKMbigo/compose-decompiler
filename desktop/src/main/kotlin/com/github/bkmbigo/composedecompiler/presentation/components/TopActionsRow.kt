package com.github.bkmbigo.composedecompiler.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.composedecompiler.data.ComposeDecompilerBaseState
import com.github.bkmbigo.composedecompiler.data.DecompilerRunState
import com.github.bkmbigo.composedecompiler.desktop.desktop.generated.resources.Res
import com.github.bkmbigo.composedecompiler.presentation.LocalDarkTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopActionsRow(
    baseState: ComposeDecompilerBaseState,
    onToggleDarkMode: () -> Unit,
    onCompile: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            val animatedRotation = rememberInfiniteTransition("Compile Rotation")

            val iconRotation by animatedRotation.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )

            IconButton(
                onClick = onToggleDarkMode
            ) {
                Icon(
                    imageVector = if (LocalDarkTheme.current)
                        Icons.Default.LightMode
                    else
                        Icons.Default.DarkMode,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                onClick = {
                    if (baseState.decompilerRunState == DecompilerRunState.Ready) {
                        onCompile()
                    }
                },
                modifier = Modifier
                    .graphicsLayer {
                        when (baseState.decompilerRunState) {
                            DecompilerRunState.Ready -> { /* no-op */
                            }

                            DecompilerRunState.Building -> {
                                rotationZ = iconRotation
                            }
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.compile),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }


            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                onClick = {
                    onOpenSettings()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
            }
        }
    }

}
