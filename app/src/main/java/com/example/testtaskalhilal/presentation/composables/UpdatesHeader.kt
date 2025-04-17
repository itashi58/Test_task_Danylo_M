package com.example.testtaskalhilal.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.testtaskalhilal.R
import com.example.testtaskalhilal.ui.theme.White

@Composable
fun UpdatesHeader(
    lastUpdatedTime: String,
    isConnectionIssues: Boolean,
    onUpdateSyncClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isConnectionIssues) {
                IconButton(onClick = { onUpdateSyncClick() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh connection icon"
                    )
                }
            }
        }

        Text(
            text = "Last Updated: $lastUpdatedTime",
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(R.dimen.double_padding),
                    horizontal = dimensionResource(R.dimen.default_padding)
                )
        )
    }
}

@Preview
@Composable
private fun LiveUpdatesHeaderPreview() {
    UpdatesHeader("12:00:01 14/04", false) { }
}