# Android Backstack Demo App

A comprehensive Android application demonstrating the usage and behavior of the Navigation Component backstack in Jetpack Compose.

## Features

This app provides an interactive demonstration of Android's navigation backstack with:

### 🎯 Core Features
- **Visual Backstack Representation**: Real-time visualization of the navigation backstack
- **Multiple Screens**: 5 different screens (Home + Screen A/B/C/D) for complex navigation flows
- **Interactive Navigation**: Buttons to navigate between screens with different navigation options
- **Backstack State Tracking**: ViewModel-based state management to track and display the current backstack

### 📚 Navigation Concepts Demonstrated

1. **Normal Navigation**
   - Standard navigation that adds screens to the backstack
   - Each navigation creates a new entry in the stack

2. **Pop Up To**
   - Removes screens from the backstack until a specified destination
   - Useful for clearing intermediate screens in a flow
   - Example: Navigate to Home and clear all screens in between

3. **Single Top**
   - Reuses existing screen instance if it's on top of the stack
   - Prevents duplicate screens in the backstack
   - Useful for preventing multiple instances of the same screen

4. **Clear Stack**
   - Demonstrates `popUpTo` with `inclusive = true`
   - Completely clears the backstack to reset navigation state
   - Useful for flows like logout or completing a checkout process

5. **Back Navigation**
   - Programmatic back navigation using `popBackStack()`
   - Works in conjunction with system back button/gesture
   - Follows LIFO (Last In, First Out) principle

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Navigation**: Navigation Compose
- **Architecture**: MVVM with ViewModel
- **State Management**: StateFlow
- **Material Design**: Material 3

## Project Structure

```
app/src/main/java/com/example/backstack/
├── MainActivity.kt                 # Main activity with NavHost setup
├── BackstackViewModel.kt          # ViewModel for backstack state
├── navigation/
│   └── Screen.kt                  # Navigation route definitions
└── screens/
    ├── CommonComponents.kt        # Reusable UI components
    ├── HomeScreen.kt             # Home screen composable
    ├── ScreenA.kt                # Screen A composable
    ├── ScreenB.kt                # Screen B composable
    ├── ScreenC.kt                # Screen C composable
    └── ScreenD.kt                # Screen D composable
```

## How to Use

1. **Build and Run**: Open the project in Android Studio and run the app
2. **Navigate**: Use the buttons to navigate between different screens
3. **Observe Backstack**: Watch the backstack visualizer update in real-time
4. **Try Different Options**: Experiment with different navigation options (Normal, Pop Up To, Single Top)
5. **Use Back Button**: Try the system back button or the "Go Back" buttons to see how screens are popped from the stack

## Key Components

### BackstackViewModel
Manages the backstack state using StateFlow, allowing screens to observe and display the current navigation stack.

### CommonComponents
Provides reusable UI components including:
- `BackstackVisualizer`: Visual representation of the current backstack
- `ScreenHeader`: Colored header for each screen
- `NavigationButton`: Styled buttons for navigation actions
- `InfoCard`: Informational cards explaining concepts

### Navigation Setup
The app uses Navigation Compose with:
- Sealed class for type-safe routes
- LaunchedEffect to track backstack changes
- Various navigation options demonstrated through different button actions

## Learning Points

This app helps developers understand:

1. **How the backstack grows**: Each navigation typically adds a new entry
2. **How the backstack shrinks**: Back navigation removes the top entry
3. **When to clear the backstack**: Using popUpTo for flow completion
4. **When to reuse screens**: Using launchSingleTop to prevent duplicates
5. **Visual feedback**: How to represent navigation state in the UI

## Requirements

- Android Studio Iguana or later
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 36
- Kotlin 2.0.21
- Gradle 8.13.1

## Dependencies

```kotlin
implementation("androidx.navigation:navigation-compose:2.8.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
// Other standard Jetpack Compose dependencies
```

## Building the Project

```bash
./gradlew build
```

## Running Tests

```bash
./gradlew test
```

## Contributing

This is a demo project for learning purposes. Feel free to fork and experiment!

## License

This project is open source and available for educational purposes.

## Author

Created as a demonstration of Android Navigation Component backstack functionality.

