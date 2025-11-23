package com.example.backstack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScreenD(
    backstack: List<String>,
    onNavigateToA: () -> Unit,
    onNavigateToB: () -> Unit,
    onNavigateToC: () -> Unit,
    onNavigateToHomeAndClear: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            title = "📄 SCREEN D",
            subtitle = "Fourth Level Navigation",
            color = Color(0xFF4CC9F0)
        )
        
        BackstackVisualizer(
            backstack = backstack,
            currentScreen = "Screen D"
        )
        
        InfoCard(
            title = "Current Screen: D",
            description = "You're on Screen D, the deepest screen in this demo. " +
                    "Notice how the backstack shows all previous screens you've navigated through."
        )
        
        InfoCard(
            title = "Backstack Management",
            description = "Good navigation design considers when to preserve and when to clear the backstack. " +
                    "For example, after completing a flow (like checkout), you might want to clear the " +
                    "intermediate screens and return to the home screen."
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
            text = "🏠 Go to Home & Clear Stack",
            onClick = onNavigateToHomeAndClear
        )
        
        InfoCard(
            title = "Clear Stack Explanation",
            description = "The 'Go to Home & Clear Stack' button demonstrates popUpTo with inclusive = true. " +
                    "This clears the entire backstack including the current screen, " +
                    "effectively resetting the navigation state to just the Home screen."
        )
        
        NavigationButton(
            text = "⬅️ Go Back",
            onClick = onBack
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

