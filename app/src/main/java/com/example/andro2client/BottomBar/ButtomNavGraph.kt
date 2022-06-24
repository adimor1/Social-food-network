package com.example.andro2client.BottomBar


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.andro2client.AddRecipeView
import com.example.andro2client.BookScreenView
import com.example.andro2client.HomeScreenView
import com.example.andro2client.ProfileView

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