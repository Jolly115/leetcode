package com.example.backstack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScreenA(
    backstack: List<String>,
    onNavigateToB: () -> Unit,
    onNavigateToC: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToHomePopUpTo: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            title = "📄 SCREEN A",
            subtitle = "First Level Navigation",
            color = Color(0xFF03DAC5)
        )
        
        BackstackVisualizer(
            backstack = backstack,
            currentScreen = "Screen A"
        )
        
        InfoCard(
            title = "Current Screen: A",
            description = "You're on Screen A. Try navigating to other screens to see how the backstack grows. " +
                    "Notice how each navigation adds a new entry to the backstack."
        )
        
        NavigationButton(
            text = "Navigate to Screen B (Normal)",
            onClick = onNavigateToB
        )
        
        NavigationButton(
            text = "Navigate to Screen C",
            onClick = onNavigateToC
        )
        
        NavigationButton(
            text = "Navigate to Home (Normal)",
            onClick = onNavigateToHome
        )
        
        NavigationButton(
            text = "Navigate to Home (Pop Up To)",
            onClick = onNavigateToHomePopUpTo
        )
        
        InfoCard(
            title = "Pop Up To Explanation",
            description = "The 'Pop Up To' button will navigate to Home and clear the entire backstack up to Home. " +
                    "This is useful for resetting navigation flow or implementing 'Clear Task' behavior."
        )
        
        NavigationButton(
            text = "⬅️ Go Back",
            onClick = onBack
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

