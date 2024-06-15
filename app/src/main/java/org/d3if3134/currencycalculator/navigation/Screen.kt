package org.d3if3134.currencycalculator.navigation

sealed class Screen(val route: String) {
    data object Login: Screen("loginScreen")
    data object Profile: Screen("profileScreen")
    data object Home: Screen("mainScreen")
    data object Detail: Screen("detailScreen")
    data object History: Screen("historyScreen")
    data object UpdateCurrency: Screen("updateCurrencyScreen/{id_currency}") {
        fun withId(id: Long) = "updateCurrencyScreen/$id"
    }
}