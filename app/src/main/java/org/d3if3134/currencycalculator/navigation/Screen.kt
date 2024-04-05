package org.d3if3134.currencycalculator.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object Detail: Screen("detailScreen")
}