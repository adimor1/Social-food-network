package com.example.andro2client


import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@ExperimentalFoundationApi
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
        composable(route = BottomBarScreen.Profile.route) {
            ProfileView()
        }
        composable(route = BottomBarScreen.Book.route) {
            BookScreenView()
        }
    }

}