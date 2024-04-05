package org.d3if3134.currencycalculator.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.d3if3134.currencycalculator.model.CurrencyCode

@Composable
fun ListItemDetail(currency: CurrencyCode) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ){
        Row {
            Image(
                painter = painterResource(id = currency.flag),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
                    .size(80.dp)
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
                    Text(text = "1 USD = 1 ${currency.code}")
                } else if (currency.code == "IDR") {
                    Text(text = "1 USD = 14,000 ${currency.code}")
                } else if (currency.code == "MYR") {
                    Text(text = "1 USD = 4.2 ${currency.code}")
                } else if (currency.code == "SAR") {
                    Text(text = "1 USD = 3.75 ${currency.code}")
                } else if (currency.code == "SGD") {
                    Text(text = "1 USD = 1.35 ${currency.code}")
                }
            }

        }
    }
}


