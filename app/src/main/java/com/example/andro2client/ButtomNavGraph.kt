package com.example.andro2client


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Add.route) {
            AddRecipeView()
        }
        composable(route = BottomBarScreen.Home.route) {
            HomeScreenView()
        }
    }

}