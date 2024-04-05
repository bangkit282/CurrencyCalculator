package org.d3if3134.currencycalculator.model

import androidx.annotation.DrawableRes

data class CurrencyCode(
    val currency: String,
    val code: String,
    @DrawableRes val flag: Int,
)
