package com.github.bkmbigo.composedecompiler.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.bkmbigo.composedecompiler.data.InformationBannerState

@Composable
fun InformationBanner(
    informationBannerState: InformationBannerState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .weight(1f, true),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = informationBannerState.mesage,
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                )
            }

            // Spacer is to ensure components do not converge
            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null
                )
            }
        }
    }
}
