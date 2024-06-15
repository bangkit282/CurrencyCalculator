package org.d3if3134.currencycalculator.ui.screen

import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3134.currencycalculator.R
import org.d3if3134.currencycalculator.database.CurrencyDb


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun UpdateCurrencyScreenPreview() {
    UpdateCurrencyScreen(rememberNavController())
}

const val KEY_ID_CURRENCY = "id_currency"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCurrencyScreen(navController : NavHostController, id: Long? = null) {
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
                title = { Text(text = "Update Currency") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )

            )
        }
    ) { innerPadding ->
        UpdateCurrencyContent(
            id = id ?: 1,
            modifier = Modifier.
            padding(innerPadding),
            navController
        )
    }
}

@Composable
fun UpdateCurrencyContent(
    id: Long,
    modifier: Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    val db = CurrencyDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data = viewModel.data.value
    val currentCurrency = data.find { it.id == id }
    val code = currentCurrency?.code ?: ""
    val rates = currentCurrency?.rate.toString()
    var amount by rememberSaveable { mutableStateOf(rates) }
    var showDialog by rememberSaveable { mutableStateOf(false) }


    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (code == "USD"){
            Image(
                painter = painterResource(id = R.drawable.usa),
                contentDescription = null,
                Modifier
                    .padding(16.dp)
                    .border(5.dp, MaterialTheme.colorScheme.onSurface))
        }
        if (code == "IDR"){
            Image(
                painter = painterResource(id = R.drawable.indonesia),
                contentDescription = null,
                Modifier
                    .padding(16.dp)
                    .border(5.dp, MaterialTheme.colorScheme.onSurface))
        }
        if (code == "MYR"){
            Image(
                painter = painterResource(id = R.drawable.malaysia),
                contentDescription = null,
                Modifier
                    .padding(16.dp)
                    .border(5.dp, MaterialTheme.colorScheme.onSurface))
        }
        if (code == "SAR"){
            Image(
                painter = painterResource(id = R.drawable.arab),
                contentDescription = null,
                Modifier
                    .padding(16.dp)
                    .border(5.dp, MaterialTheme.colorScheme.onSurface))
        }
        if (code == "SGD"){
            Image(
                painter = painterResource(id = R.drawable.singapore),
                contentDescription = null,
                Modifier
                    .padding(16.dp)
                    .border(5.dp, MaterialTheme.colorScheme.onSurface))
        }
        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Rate") },
            modifier = Modifier.padding(16.dp))

        Button(onClick = {
            showDialog = true
        }) {
            Text("Update")
        }

        if (showDialog) { // Show dialog when showDialog is true
            Dialog(onDismissRequest = { showDialog = false }) {
                Card(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Are you sure want to update this currency?")
                        Spacer(modifier = Modifier.size(16.dp))
                        Row {
                            Button(onClick = {
                                viewModel.update(amount, id)
                                showDialog = false
                                navController.popBackStack()
                                Handler(
                                    Looper.getMainLooper()).postDelayed({
                                Toast.makeText(context, "Currency updated", Toast.LENGTH_SHORT).show()
                            }, 500)
                            }) {
                                Text("Yes")
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            Button(onClick = {
                                showDialog = false
                            }) {
                                Text("No")
                            }
                        }
                    }
                }
            }
        }
    }
}
