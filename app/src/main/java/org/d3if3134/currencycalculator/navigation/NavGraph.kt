package org.d3if3134.currencycalculator.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if3134.currencycalculator.ui.screen.DetailScreen
import org.d3if3134.currencycalculator.ui.screen.HistoryScreen
import org.d3if3134.currencycalculator.ui.screen.KEY_ID_CURRENCY
import org.d3if3134.currencycalculator.ui.screen.LoginScreen
import org.d3if3134.currencycalculator.ui.screen.MainScreen
import org.d3if3134.currencycalculator.ui.screen.ProfileScreen
import org.d3if3134.currencycalculator.ui.screen.UpdateCurrencyScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Home.route) {
            MainScreen(navController)
        }
        composable(
            route = Screen.Detail.route
        ) {
            DetailScreen(navController)
        }
        composable(
            route = Screen.History.route
        ) {
            HistoryScreen(navController)
        }
        composable(
            route = Screen.UpdateCurrency.route,
            arguments = listOf(navArgument(KEY_ID_CURRENCY) { type = NavType.LongType })
        ) {
            val id = it.arguments?.getLong(KEY_ID_CURRENCY)
            UpdateCurrencyScreen(navController, id)
        }
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navController)
        }
        composable(
            route = Screen.Profile.route
        ) {
            ProfileScreen(navController)
        }

    }
}