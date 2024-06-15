package org.d3if3134.currencycalculator.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.d3if3134.currencycalculator.model.CurrencyCode
import org.d3if3134.currencycalculator.model.History

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCurrencyCode(currencyCode: CurrencyCode)

    @Query("SELECT * FROM currency_code")
    fun getAllCurrencyCode(): Flow<List<CurrencyCode>>

    @Query("SELECT * FROM currency_code WHERE id = :id")
    fun getCurrencyCode(id: Long): Flow<List<CurrencyCode>>

    @Query("Select * from currency_code where code = :code")
    suspend fun getCurrencyByCode(code: String): CurrencyCode

    @Query("UPDATE currency_code SET rate = :rates WHERE id = :id")
    fun updateCurrencyCode(rates: String, id: Long)

    @Query("Insert into history (fromCurrency, toCurrency, amount, result) values (:fromCurrencys, :toCurrencys, :amounts, :results)")
    suspend fun insertHistory(fromCurrencys: String, toCurrencys: String, amounts: Double, results: Double)

    @Query("SELECT * FROM history")
    fun getAllHistory(): Flow<List<History>>

    @Query("DELETE From history")
    suspend fun deleteAllHistory()

    @Query("DELETE FROM history WHERE id = :ids")
    suspend fun deleteHistory(ids: Long)

}