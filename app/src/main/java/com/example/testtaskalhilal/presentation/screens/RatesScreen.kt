import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testtaskalhilal.R
import com.example.testtaskalhilal.domain.model.DataSource
import com.example.testtaskalhilal.domain.model.Rate
import com.example.testtaskalhilal.presentation.viewmodels.RatesViewModel
import com.example.testtaskalhilal.presentation.composables.UpdatesHeader
import com.example.testtaskalhilal.presentation.composables.RateItem
import com.example.testtaskalhilal.ui.theme.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RatesScreen(
    ratesViewModel: RatesViewModel = hiltViewModel()
) {
    val searchQuery by ratesViewModel.searchQuery.collectAsStateWithLifecycle()
    val ratesData by ratesViewModel.rates.collectAsStateWithLifecycle()

    // To retry fetching rates when the screen is opened and to stop fetching when the screen is closed
    DisposableEffect(Unit) {
        ratesViewModel.restartFetchRates()
        onDispose {
            ratesViewModel.stopFetchRates()
        }
    }

    // Show toast if data is from local database
    val context = LocalContext.current
    LaunchedEffect(ratesData.dataSource) {
        if (ratesData.dataSource is DataSource.Local) {
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
            Column(
                modifier = Modifier.background(White)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { ratesViewModel.updateSearchQuery(it) },
                    label = { Text(stringResource(R.string.rates_search)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen.default_padding))
                )
                UpdatesHeader(ratesData.lastUpdatedTime, ratesData.dataSource == DataSource.Local) {
                    ratesViewModel.restartFetchRates()
                }
            }
        }
        items(items = ratesData.rates, key = { it.shortName }) { rate ->
            RateItem(rate = rate) { rate ->
                ratesViewModel.toggleFavorite(rate)
            }
        }
    }
}




@Composable
@Preview
private fun RatesItemPreview() {
    RateItem(rate = Rate("USD", "United States Dollar", "1.0", true)) {}
}