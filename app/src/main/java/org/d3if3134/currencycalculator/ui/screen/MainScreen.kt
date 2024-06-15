package org.d3if3134.currencycalculator.ui.screen

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if3134.currencycalculator.R
import org.d3if3134.currencycalculator.database.CurrencyDb
import org.d3if3134.currencycalculator.model.CurrencyCode
import org.d3if3134.currencycalculator.navigation.Screen
import org.d3if3134.currencycalculator.ui.theme.CurrencyCalculatorTheme


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    CurrencyCalculatorTheme {
        MainScreen(rememberNavController())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController){
//    var menuExpanded by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Currency Calculator") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.History.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_history_24),
                            contentDescription = stringResource(id = R.string.history_screen),
                            tint = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.Detail.route) }) {
                        Icon(
                            imageVector = Icons.Rounded.List,
                            contentDescription = stringResource(id = R.string.detail_screen),
                            tint = contentColorFor(MaterialTheme.colorScheme.primaryContainer)

                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(modifier = Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(modifier: Modifier) {
    val context = LocalContext.current
    val db = CurrencyDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data = viewModel.data.value
    val firebase = FirebaseFirestore.getInstance()


    if (data.isEmpty()) {
        viewModel.retrieveData()
    }

    if (data.isNotEmpty()) {

        var amount by rememberSaveable { mutableFloatStateOf(0f) }
        var from by remember { mutableStateOf(data[0]) }
        var to by remember { mutableStateOf(data[1]) }
        var isExpanded by remember { mutableStateOf(false) }
        var isExpandedTo by remember { mutableStateOf(false) }
        var result by rememberSaveable { mutableStateOf("") }
        var isVisibility by rememberSaveable { mutableStateOf(false) }


        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(id = R.string.currencyconverter))

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.selectfrom))

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded }) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = from.currency,
                    onValueChange = {},
                    label = { Text(text = stringResource(id = R.string.from)) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }) {
                    data.forEach { currency ->
                        Box(
                            modifier = Modifier
                                .height(56.dp)
                                .clickable { from = currency; isExpanded = false }
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 16.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text = currency.currency)
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(id = R.string.selectto))

            ExposedDropdownMenuBox(
                expanded = isExpandedTo,
                onExpandedChange = { isExpandedTo = !isExpandedTo }) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = to.currency,
                    onValueChange = {},
                    label = { Text(text = stringResource(id = R.string.to)) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedTo) }
                )

                ExposedDropdownMenu(
                    expanded = isExpandedTo,
                    onDismissRequest = { isExpandedTo = false }) {
                    data.forEach { currency ->
                        Box(
                            modifier = Modifier
                                .height(56.dp)
                                .clickable { to = currency; isExpandedTo = false }
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 16.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text = currency.currency)
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = amount.toString(),
                onValueChange = {
                    amount = it.toFloat()
                },
                label = { Text(text = stringResource(id = R.string.amount)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                result = calculateCurrency(amount, from, to)
                isVisibility = true
                viewModel.insertHistory(from.currency, to.currency, amount.toDouble(), result.toDouble())

                val history: Map<String, Any> = hashMapOf(
                    "fromCurrency" to from.currency,
                    "toCurrency" to to.currency,
                    "amount" to amount.toDouble(),
                    "result" to result.toDouble()
                )

                firebase.collection("history")
                    .add(history)
                    .addOnSuccessListener {
                        viewModel.retrieveData()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error writing document", e)
                    }
            }) {
                Text(text = stringResource(id = R.string.calculate))
            }

            Spacer(modifier = Modifier.height(48.dp))

            if (isVisibility) {
                Text(
                    text = stringResource(id = R.string.result),
                    color = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
                )
                Text(
                    text = result + " " + to.code,
                    style = MaterialTheme.typography.headlineLarge,
                    color = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
                )

                Spacer(modifier = Modifier.height(48.dp))

                if (result != "Select different currency!") {
                    Button(
                        onClick = {
                            shareData(
                                context = context,
                                amount.toString() + " = " + result + " " + to.code + "\n CHECK IT OUT FROM CURRENCY CALCULATOR APP!"
                            )
                        }) {
                        Text(text = stringResource(id = R.string.share))
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

private fun calculateCurrency(
    amount: Float,
    fromCurrency: CurrencyCode,
    toCurrency: CurrencyCode
) : String {
    var result = 0f
    if (fromCurrency.code == "USD" && toCurrency.code == "IDR") {
        result = amount * 14000
        return result.toString()
    } else if (fromCurrency.code == "IDR" && toCurrency.code == "USD") {
        result = amount / 14000
        return result.toString()
    } else if (fromCurrency.code == "USD" && toCurrency.code == "MYR") {
        result = amount * 4.13f
        return result.toString()
    } else if (fromCurrency.code == "MYR" && toCurrency.code == "USD") {
        result = amount / 4.13f
        return result.toString()
    } else if (fromCurrency.code == "USD" && toCurrency.code == "SAR") {
        result = amount * 3.75f
        return result.toString()
    } else if (fromCurrency.code == "SAR" && toCurrency.code == "USD") {
        result = amount / 3.75f
        return result.toString()
    } else if (fromCurrency.code == "USD" && toCurrency.code == "SGD") {
        result = amount * 1.35f
        return result.toString()
    } else if (fromCurrency.code == "SGD" && toCurrency.code == "USD") {
        result = amount / 1.35f
        return result.toString()
    } else if (fromCurrency.code == "IDR" && toCurrency.code == "MYR") {
        result = amount * 0.00029f
        return result.toString()
    } else if (fromCurrency.code == "MYR" && toCurrency.code == "IDR") {
        result = amount / 0.00029f
        return result.toString()
    } else if (fromCurrency.code == "IDR" && toCurrency.code == "SAR") {
        result = amount * 0.00027f
        return result.toString()
    } else if (fromCurrency.code == "SAR" && toCurrency.code == "IDR") {
        result = amount / 0.00027f
        return result.toString()
    } else if (fromCurrency.code == "IDR" && toCurrency.code == "SGD") {
        result = amount * 0.000095f
        return result.toString()
    } else if (fromCurrency.code == "SGD" && toCurrency.code == "IDR") {
        result = amount / 0.000095f
        return result.toString()
    } else if (fromCurrency.code == "MYR" && toCurrency.code == "SAR") {
        result = amount * 0.73f
        return result.toString()
    } else if (fromCurrency.code == "SAR" && toCurrency.code == "MYR") {
        result = amount / 0.73f
        return result.toString()
    } else if (fromCurrency.code == "MYR" && toCurrency.code == "SGD") {
        result = amount * 0.36f
        return result.toString()
    } else if (fromCurrency.code == "SGD" && toCurrency.code == "MYR") {
        result = amount / 0.36f
        return result.toString()
    } else if (fromCurrency.code == "SAR" && toCurrency.code == "SGD") {
        result = amount * 0.41f
        return result.toString()
    } else if (fromCurrency.code == "SGD" && toCurrency.code == "SAR") {
        result = amount / 0.41f
        return result.toString()
    } else if (fromCurrency.code == toCurrency.code) {
        return "Select different currency!"
    }
    return result.toString()

}

private fun shareData(context: Context, message: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}



