package org.d3if3134.currencycalculator.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.d3if3134.currencycalculator.R
import org.d3if3134.currencycalculator.data.currencyCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController : NavHostController) {
    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = { Text(text = stringResource(id = R.string.detail_screen)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )

            )
        }
    ) { innerPadding ->
        DetailContent(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun DetailContent(modifier: Modifier = Modifier) {
    val currency = remember { currencyCode }
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(16.dp))
    {
        items(
            items = currency,
            itemContent = { currency ->
                ListItemDetail(currency = currency)
            }
        )
    }
}

