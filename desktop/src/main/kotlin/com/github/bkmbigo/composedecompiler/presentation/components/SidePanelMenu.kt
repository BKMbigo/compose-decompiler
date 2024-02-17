package com.github.bkmbigo.composedecompiler.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.bkmbigo.composedecompiler.data.ComposeDecompilerPage
import com.github.bkmbigo.composedecompiler.desktop.desktop.generated.resources.Res
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SidePanelMenu(
    currentDestination: ComposeDecompilerPage,
    onDestinationChanged: (ComposeDecompilerPage) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            SidePanelMenuItem(
                isCurrentDestination = currentDestination == ComposeDecompilerPage.StabilityInferencer,
                destination = ComposeDecompilerPage.StabilityInferencer,
                onDestinationChanged = {
                    onDestinationChanged(ComposeDecompilerPage.StabilityInferencer)
                },
                icon = Res.drawable.stability,
                title = "Stability Inferencer",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )

            SidePanelMenuItem(
                isCurrentDestination = currentDestination == ComposeDecompilerPage.IrDecompiler,
                destination = ComposeDecompilerPage.IrDecompiler,
                onDestinationChanged = {
                    onDestinationChanged(ComposeDecompilerPage.IrDecompiler)
                },
                icon = Res.drawable.code,
                title = "Compose Decompiler",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun SidePanelMenuItem(
    isCurrentDestination: Boolean,
    destination: ComposeDecompilerPage,
    onDestinationChanged: (ComposeDecompilerPage) -> Unit,
    icon: DrawableResource,
    title: String,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        onClick = {
            if (!isCurrentDestination) {
                onDestinationChanged(destination)
            }
        },
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            contentColor = if (isCurrentDestination)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onSurface,
            containerColor = if (isCurrentDestination)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
