package com.example.backstack.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ScreenA : Screen("screen_a")
    object ScreenB : Screen("screen_b")
    object ScreenC : Screen("screen_c")
    object ScreenD : Screen("screen_d")
}

