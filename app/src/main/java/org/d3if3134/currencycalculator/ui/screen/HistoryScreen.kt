package org.d3if3134.currencycalculator.ui.screen

import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.d3if3134.currencycalculator.R
import org.d3if3134.currencycalculator.database.CurrencyDb
import org.d3if3134.currencycalculator.model.History
import org.d3if3134.currencycalculator.navigation.Screen
import org.d3if3134.currencycalculator.ui.theme.CurrencyCalculatorTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun HistoryScreenPreview() {
    CurrencyCalculatorTheme {
        HistoryScreen(rememberNavController())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavHostController) {
    var showClearDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = CurrencyDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)

    val firebase = Firebase.firestore
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
                title = { Text(text = stringResource(id = R.string.history_screen)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions=  {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = stringResource(id = R.string.delete_all),
                            tint = contentColorFor(MaterialTheme.colorScheme.primaryContainer)

                        )
                    }
                }
            )

            if (showClearDialog) {
                Dialog(onDismissRequest = { showClearDialog = false }) {
                    Card(modifier = Modifier.padding(16.dp))
                    {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(id = R.string.delete_all_history))
                            Spacer(modifier = Modifier.size(16.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(onClick = {
                                    viewModel.deleteAllHistory()
                                    firebase.collection("history").get()
                                        .addOnSuccessListener { result ->
                                            for (document in result) {
                                                firebase.collection("history").document(document.id).delete()
                                                    .addOnSuccessListener {
                                                        Toast.makeText(context, "Semua histori berhasil terhapus", Toast.LENGTH_SHORT).show()
                                                        navController.navigate(Screen.History.route)
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Toast.makeText(context, "Histori gagal terhapus", Toast.LENGTH_SHORT).show()
                                                    }
                                            }
                                            showClearDialog = false
                                            Toast.makeText(context, context.getText(R.string.delete_all), Toast.LENGTH_SHORT).show()
                                        }
                                }) {
                                    Text(text = stringResource(id = R.string.yes) )

                                }
                                TextButton(onClick = { showClearDialog = false }) {
                                    Text(text = stringResource(id = R.string.no))
                                }
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.Home.route)
                    Handler(
                        Looper.getMainLooper()).postDelayed({
                    Toast.makeText(context, context.getText(R.string.add_data), Toast.LENGTH_SHORT).show()
                }, 500)
                }
            )
            {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        HistoryContent(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun HistoryContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val db = CurrencyDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data = viewModel.getAllHistory().collectAsState(initial = emptyList()).value
    var showDeleteDialog by remember { mutableStateOf(false) }

    val firebase = Firebase.firestore
    var historyList = remember { mutableStateOf(emptyList<History>()) }

    // Gunakan LaunchedEffect untuk memanggil fungsi suspend dalam Composable
    LaunchedEffect(key1 = Unit) {
        firebase.collection("history").get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<History>()
                for (document in result) {
                    val history = document.toObject(History::class.java)
                    list.add(history)
                }
                historyList.value = list
            }
    }

    if(historyList.value.isEmpty())
    {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        )
        {
            Image(
                painter = painterResource(id = R.drawable.empty_data),
                contentDescription = null,
                modifier = Modifier.size(100.dp))

            Text(text = "No History")
        }
    }
    else{
        LazyColumn(modifier = modifier, contentPadding = PaddingValues(bottom = 84.dp))
        {
            items(historyList.value) { history ->
                ListItemHistory(history = history, onClick = {})
            }
        }
    }
    if (showDeleteDialog) {
        Dialog(onDismissRequest = { showDeleteDialog = false }) {
            Card(modifier = Modifier.padding(16.dp))
            {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.delete_history))
                    Spacer(modifier = Modifier.size(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()


                    ) {
                        TextButton(onClick = {
                            viewModel.deleteHistory(data[0].id)
                            showDeleteDialog = false
                            Toast.makeText(context, context.getText(R.string.delete), Toast.LENGTH_SHORT).show()
                        }) {
                        }
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text(text = stringResource(id = R.string.noo))
                        }
                    }
                }
            }
        }
    }
}
