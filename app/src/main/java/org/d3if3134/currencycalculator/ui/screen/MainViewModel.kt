package org.d3if3134.currencycalculator.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.d3if3134.currencycalculator.R
import org.d3if3134.currencycalculator.database.CurrencyDao
import org.d3if3134.currencycalculator.model.CurrencyCode
import org.d3if3134.currencycalculator.network.ApiStatus
import org.d3if3134.currencycalculator.network.CurrencyApi

class MainViewModel(private val dao: CurrencyDao): ViewModel(){

    val db = Firebase.firestore

    var data = mutableStateOf(emptyList<CurrencyCode>())

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = CurrencyApi.service.getCurrency()
                status.value = ApiStatus.SUCCESS
                Log.d("MainViewModel", "Success: ${data.value}")
            } catch (e: Exception) {
                status.value = ApiStatus.FAILED
                Log.d("MainViewModel", "Failure: ${e.message}")

            }
        }
    }

//    val data: StateFlow<List<CurrencyCode>> = dao.getAllCurrencyCode().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(),
//        initialValue = emptyList()
//    )

//    fun insertCurrencyCode(){
//        viewModelScope.launch(Dispatchers.IO) {
//            dao.insertCurrencyCode(
//                CurrencyCode(1, "US Dollar", "USD", "1.0", R.drawable.usa),
//            )
//            dao.insertCurrencyCode(
//                CurrencyCode(2,"Indonesian Rupiah", "IDR", "14000.0", R.drawable.indonesia),
//            )
//            dao.insertCurrencyCode(
//                CurrencyCode(3,"Malaysian Ringgit", "MYR", "4.2", R.drawable.malaysia),
//            )
//            dao.insertCurrencyCode(
//                CurrencyCode(4,"Saudi Riyal", "SAR", "3.75", R.drawable.arab),
//            )
//            dao.insertCurrencyCode(
//                CurrencyCode(5,"Singapore Dollar", "SGD", "1.35", R.drawable.singapore),
//            )
//        }
//    }

    fun getCurrencyCode(id: Long) = dao.getCurrencyCode(id)

    suspend fun getCurrencyByCode(code: String) : CurrencyCode {
        return dao.getCurrencyByCode(code)
    }

    fun update(rate: String, id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateCurrencyCode(rate, id)
        }
    }

    fun insertHistory(fromCurrency: String, toCurrency: String, amount: Double, result: Double){
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertHistory(fromCurrency, toCurrency, amount, result)
        }
    }

    fun getAllHistory() = dao.getAllHistory()

    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAllHistory()
        }
    }

    fun deleteHistory(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteHistory(id)
        }
    }

}