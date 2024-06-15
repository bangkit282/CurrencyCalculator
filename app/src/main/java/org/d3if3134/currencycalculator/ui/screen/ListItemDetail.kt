package org.d3if3134.currencycalculator.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.room.Delete
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.d3if3134.currencycalculator.R
import org.d3if3134.currencycalculator.model.CurrencyCode
import org.d3if3134.currencycalculator.network.CurrencyApi

@Composable
fun ListItemDetail(currency: CurrencyCode, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ){
        Row {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currency.flag)
                    .build(),
                contentDescription = stringResource(R.string.image, currency.currency),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.broken_img),
                modifier = Modifier.size(100.dp)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = currency.currency)
                if (currency.code == "USD") {
                    Text(text = "1 USD = ${currency.rate} ${currency.code}")
                } else if (currency.code == "IDR") {
                    Text(text = "1 USD = ${currency.rate} ${currency.code}")
                } else if (currency.code == "MYR") {
                    Text(text = "1 USD = ${currency.rate} ${currency.code}")
                } else if (currency.code == "SAR") {
                    Text(text = "1 USD = ${currency.rate} ${currency.code}")
                } else if (currency.code == "SGD") {
                    Text(text = "1 USD = ${currency.rate} ${currency.code}")
                }
            }

        }
    }
}


