
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testtaskalhilal.R
import com.example.testtaskalhilal.domain.model.DataSource
import com.example.testtaskalhilal.presentation.viewmodels.HomeViewModel
import com.example.testtaskalhilal.presentation.composables.UpdatesHeader
import com.example.testtaskalhilal.presentation.composables.RateItem
import com.example.testtaskalhilal.ui.theme.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val favoriteRates by homeViewModel.favoriteRates.collectAsStateWithLifecycle()

    // To retry fetching rates when the screen is opened and to stop fetching when the screen is closed
    DisposableEffect(Unit) {
        homeViewModel.restartFetchRates()
        onDispose {
            homeViewModel.stopFetchRates()
        }
    }

    // Show toast if data is from local database
    val context = LocalContext.current
    LaunchedEffect(favoriteRates.dataSource) {
        if (favoriteRates.dataSource is DataSource.Local) {
            Toast.makeText(context, "Network error: Showing cached data", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(
            top = dimensionResource(R.dimen.default_padding),
            start = dimensionResource(R.dimen.default_padding),
            end = dimensionResource(R.dimen.default_padding)
        ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.default_padding))
    ) {
        stickyHeader {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .background(White),
                text = "Favorites",
                textAlign = TextAlign.Center,
                fontSize = 22.sp
            )
            UpdatesHeader(favoriteRates.lastUpdatedTime, favoriteRates.dataSource == DataSource.Local) {
                homeViewModel.restartFetchRates()
            }
        }
        items(items = favoriteRates.rates, key = { it.shortName }) { rate ->
            RateItem(rate = rate, isFavoritesList = true) { rate ->
                homeViewModel.toggleFavorite(rate)
            }
        }

        if (favoriteRates.rates.isEmpty()) {
            item {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "You have no favorite rates",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }
        }
    }
}

