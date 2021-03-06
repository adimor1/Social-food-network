package com.example.andro2client.BottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Profile : BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    object Add : BottomBarScreen(
        route = "add",
        title = "Add",
        icon = Icons.Default.AddCircle
    )

    object Book : BottomBarScreen(
        route = "book",
        title = "Book",
        icon = Icons.Default.Book
    )

}