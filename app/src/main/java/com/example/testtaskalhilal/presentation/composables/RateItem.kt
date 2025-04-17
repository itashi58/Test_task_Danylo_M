package com.example.testtaskalhilal.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.example.testtaskalhilal.R
import com.example.testtaskalhilal.domain.model.Rate
import com.example.testtaskalhilal.ui.theme.ColorPrimary

@Composable
fun RateItem(rate: Rate, isFavoritesList: Boolean = false, onFavoriteToggle: (Rate) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.default_padding))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.default_padding))
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(rate.shortName, style = MaterialTheme.typography.bodyLarge)
                Text(rate.name, style = MaterialTheme.typography.bodySmall)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(rate.value.toString(), style = MaterialTheme.typography.bodyLarge)
                IconButton(
                    onClick = {
                        onFavoriteToggle(rate)
                    }
                ) {
                    Icon(
                        imageVector =
                            if (isFavoritesList) {
                                Icons.Default.Delete
                            } else {
                                if (rate.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                            },
                        contentDescription = null,
                        tint = if (rate.isFavorite) ColorPrimary else Color.Gray
                    )
                }
            }
        }
    }
}