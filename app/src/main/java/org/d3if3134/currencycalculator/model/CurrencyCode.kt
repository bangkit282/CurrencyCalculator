package org.d3if3134.currencycalculator.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_code")
data class CurrencyCode(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val currency: String,
    val code: String,
    val rate: String,
    val flag: String
)
