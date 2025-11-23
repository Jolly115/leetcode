package com.example.backstack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScreenC(
    backstack: List<String>,
    onNavigateToA: () -> Unit,
    onNavigateToB: () -> Unit,
    onNavigateToD: () -> Unit,
    onNavigateToHome: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            title = "📄 SCREEN C",
            subtitle = "Third Level Navigation",
            color = Color(0xFFF72585)
        )
        
        BackstackVisualizer(
            backstack = backstack,
            currentScreen = "Screen C"
        )
        
        InfoCard(
            title = "Current Screen: C",
            description = "You're on Screen C. Look at the backstack above to see your navigation path. " +
                    "Each entry represents a screen you can return to using the back button."
        )
        
        InfoCard(
            title = "Deep Navigation",
            description = "As you navigate deeper into the app, the backstack grows. " +
                    "Android automatically handles the back button to pop screens off the stack " +
                    "in reverse order (LIFO - Last In, First Out)."
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
            text = "Navigate to Screen D",
            onClick = onNavigateToD
        )
        
        NavigationButton(
            text = "Jump to Home",
            onClick = onNavigateToHome
        )
        
        NavigationButton(
            text = "⬅️ Go Back",
            onClick = onBack
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

