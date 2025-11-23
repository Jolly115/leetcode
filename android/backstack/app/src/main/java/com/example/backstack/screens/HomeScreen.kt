package com.example.backstack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    backstack: List<String>,
    onNavigateToA: () -> Unit,
    onNavigateToB: () -> Unit,
    onNavigateToC: () -> Unit,
    onNavigateToD: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            title = "🏠 HOME",
            subtitle = "Android Backstack Demo",
            color = Color(0xFF6200EA)
        )
        
        BackstackVisualizer(
            backstack = backstack,
            currentScreen = "Home"
        )
        
        InfoCard(
            title = "About This Demo",
            description = "This app demonstrates how the Android Navigation backstack works. " +
                    "Navigate through screens and watch how the backstack changes. " +
                    "Use the system back button or gesture to pop screens from the stack."
        )
        
        InfoCard(
            title = "Navigation Concepts",
            description = "• Normal Navigation: Adds screen to backstack\n" +
                    "• Pop Up To: Removes screens until specified destination\n" +
                    "• Single Top: Reuses existing instance if on top\n" +
                    "• Pop Back Stack: Removes current screen"
        )
        
        NavigationButton(
            text = "Navigate to Screen A",
            onClick = onNavigateToA
        )
        
        NavigationButton(
            text = "Navigate to Screen B",
            onClick = onNavigateToB
        )
        
        NavigationButton(
            text = "Navigate to Screen C",
            onClick = onNavigateToC
        )
        
        NavigationButton(
            text = "Navigate to Screen D",
            onClick = onNavigateToD
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

