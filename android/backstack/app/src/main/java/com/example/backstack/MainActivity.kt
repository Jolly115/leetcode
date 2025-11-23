package com.example.backstack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.backstack.navigation.Screen
import com.example.backstack.screens.*
import com.example.backstack.ui.theme.BackstackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BackstackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BackstackApp()
                }
            }
        }
    }
}

@Composable
fun BackstackApp(
    viewModel: BackstackViewModel = viewModel()
) {
    val navController = rememberNavController()
    val backstackState by viewModel.backstackState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                backstack = backstackState,
                onNavigateToA = { 
                    navController.navigate(Screen.ScreenA.route)
                    viewModel.navigateTo("Screen A")
                },
                onNavigateToB = { 
                    navController.navigate(Screen.ScreenB.route)
                    viewModel.navigateTo("Screen B")
                },
                onNavigateToC = { 
                    navController.navigate(Screen.ScreenC.route)
                    viewModel.navigateTo("Screen C")
                },
                onNavigateToD = { 
                    navController.navigate(Screen.ScreenD.route)
                    viewModel.navigateTo("Screen D")
                }
            )
        }
        
        composable(Screen.ScreenA.route) {
            ScreenA(
                backstack = backstackState,
                onNavigateToB = { 
                    navController.navigate(Screen.ScreenB.route)
                    viewModel.navigateTo("Screen B")
                },
                onNavigateToC = { 
                    navController.navigate(Screen.ScreenC.route)
                    viewModel.navigateTo("Screen C")
                },
                onNavigateToHome = { 
                    navController.navigate(Screen.Home.route)
                    viewModel.navigateTo("Home")
                },
                onNavigateToHomePopUpTo = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = false
                        }
                    }
                    viewModel.navigateToWithPopUpTo("Home", "Home", inclusive = false)
                },
                onBack = { 
                    navController.popBackStack()
                    viewModel.popBackStack()
                }
            )
        }
        
        composable(Screen.ScreenB.route) {
            ScreenB(
                backstack = backstackState,
                onNavigateToA = { 
                    navController.navigate(Screen.ScreenA.route)
                    viewModel.navigateTo("Screen A")
                },
                onNavigateToC = { 
                    navController.navigate(Screen.ScreenC.route)
                    viewModel.navigateTo("Screen C")
                },
                onNavigateToD = { 
                    navController.navigate(Screen.ScreenD.route)
                    viewModel.navigateTo("Screen D")
                },
                onNavigateToASingleTop = {
                    navController.navigate(Screen.ScreenA.route) {
                        launchSingleTop = true
                    }
                    viewModel.navigateToWithSingleTop("Screen A")
                },
                onBack = { 
                    navController.popBackStack()
                    viewModel.popBackStack()
                }
            )
        }
        
        composable(Screen.ScreenC.route) {
            ScreenC(
                backstack = backstackState,
                onNavigateToA = { 
                    navController.navigate(Screen.ScreenA.route)
                    viewModel.navigateTo("Screen A")
                },
                onNavigateToB = { 
                    navController.navigate(Screen.ScreenB.route)
                    viewModel.navigateTo("Screen B")
                },
                onNavigateToD = { 
                    navController.navigate(Screen.ScreenD.route)
                    viewModel.navigateTo("Screen D")
                },
                onNavigateToHome = { 
                    navController.navigate(Screen.Home.route)
                    viewModel.navigateTo("Home")
                },
                onBack = { 
                    navController.popBackStack()
                    viewModel.popBackStack()
                }
            )
        }
        
        composable(Screen.ScreenD.route) {
            ScreenD(
                backstack = backstackState,
                onNavigateToA = { 
                    navController.navigate(Screen.ScreenA.route)
                    viewModel.navigateTo("Screen A")
                },
                onNavigateToB = { 
                    navController.navigate(Screen.ScreenB.route)
                    viewModel.navigateTo("Screen B")
                },
                onNavigateToC = { 
                    navController.navigate(Screen.ScreenC.route)
                    viewModel.navigateTo("Screen C")
                },
                onNavigateToHomeAndClear = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                    viewModel.navigateToWithPopUpTo("Home", "Home", inclusive = true)
                },
                onBack = { 
                    navController.popBackStack()
                    viewModel.popBackStack()
                }
            )
        }
    }
}