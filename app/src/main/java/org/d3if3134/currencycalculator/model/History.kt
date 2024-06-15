package org.d3if3134.currencycalculator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val fromCurrency: String = "",
    val toCurrency: String = "",
    val amount: Double = 0.0,
    val result: Double = 0.0
)