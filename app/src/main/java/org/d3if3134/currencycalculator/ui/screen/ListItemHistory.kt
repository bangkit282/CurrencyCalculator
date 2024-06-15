package org.d3if3134.currencycalculator.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.d3if3134.currencycalculator.model.History

@Composable
fun ListItemHistory(history: History, onClick: () -> Unit) {

    Card (
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
            .fillMaxWidth()
    )
    {
        Row {
            Column (
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "From: ${history.fromCurrency}",
                )
                Text(
                    text = "To: ${history.toCurrency}",
                )
            }
            Column (
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Amount: ${history.amount}",
                )
                Text(
                    text = "Result: ${history.result}",
                )
            }
        }
    }
}


