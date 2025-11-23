package com.example.backstack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScreenB(
    backstack: List<String>,
    onNavigateToA: () -> Unit,
    onNavigateToC: () -> Unit,
    onNavigateToD: () -> Unit,
    onNavigateToASingleTop: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            title = "📄 SCREEN B",
            subtitle = "Second Level Navigation",
            color = Color(0xFFFF6B35)
        )
        
        BackstackVisualizer(
            backstack = backstack,
            currentScreen = "Screen B"
        )
        
        InfoCard(
            title = "Current Screen: B",
            description = "You're on Screen B. This screen demonstrates how multiple screens stack up. " +
                    "Try navigating back and forth to see the backstack in action."
        )
        
        NavigationButton(
            text = "Navigate to Screen A (Normal)",
            onClick = onNavigateToA
        )
        
        NavigationButton(
            text = "Navigate to Screen A (Single Top)",
            onClick = onNavigateToASingleTop
        )
        
        InfoCard(
            title = "Single Top Explanation",
            description = "If you came from Screen A, clicking 'Single Top' will reuse that existing instance " +
                    "instead of creating a new one. This prevents duplicate screens in the backstack."
        )
        
        NavigationButton(
            text = "Navigate to Screen C",
            onClick = onNavigateToC
        )
        
        NavigationButton(
            text = "Navigate to Screen D",
            onClick = onNavigateToD
        )
        
        NavigationButton(
            text = "⬅️ Go Back",
            onClick = onBack
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

