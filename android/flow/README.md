# Kotlin Flow Demo App 🌊

A comprehensive Android application demonstrating Kotlin Flow concepts including Cold Flows, Hot Flows (StateFlow & SharedFlow), and various Flow operators using traditional Android Views (XML + ViewBinding).

## 📱 Features

### 1. **StateFlow Counter**
- Real-time counter demonstration
- Shows how StateFlow holds and emits state updates
- Increment, decrement, and reset operations

### 2. **Basic Flow (Cold Flow)**
- Demonstrates cold flow behavior
- Each collection starts a new flow
- Shows sequential emission with delays

### 3. **Flow Operators**
- **Map Operator**: Transforms each emitted value
- **Filter Operator**: Filters values based on conditions
- **Combine Operator**: Combines multiple flows
- **Zip Operator**: Pairs values from two flows
- **FlatMapConcat**: Flattens nested flows sequentially
- **DistinctUntilChanged**: Emits only when value changes

### 4. **SharedFlow**
- Demonstrates hot flow broadcasting
- Multiple collectors receive same emissions
- Configurable replay cache (last 2 values)

### 5. **Advanced Operators**
- **Take Operator**: Limits number of emissions
- **Error Handling**: Retry and Catch operators
- **Buffer Operator**: Handles backpressure

## 🏗️ Architecture

```
app/
├── FlowDemoViewModel.kt      # ViewModel with all Flow logic
├── MainActivity.kt            # UI implementation with ViewBinding
├── FlowDataAdapter.kt         # RecyclerView adapter for displaying data
└── res/
    └── layout/
        ├── activity_main.xml      # Main layout
        ├── flow_demo_card.xml     # Reusable card layout
        └── item_flow_data.xml     # RecyclerView item layout
```

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest version)
- Android SDK (minSdk 24+)
- Kotlin 2.0.21+

### Installation
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on emulator or physical device

### Build
```bash
./gradlew build
```

### Run
```bash
./gradlew installDebug
```

## 📚 Learning Resources

See [FLOW_CONCEPTS.md](FLOW_CONCEPTS.md) for detailed explanations of:
- Flow fundamentals
- StateFlow vs SharedFlow
- All Flow operators with examples
- Common patterns and best practices
- Performance tips
- Common pitfalls

## 🎯 Key Concepts Demonstrated

### Cold Flow
```kotlin
fun createBasicFlow(): Flow<String> = flow {
    for (i in 1..5) {
        delay(500)
        emit("Item $i")
    }
}
```

### StateFlow
```kotlin
private val _counter = MutableStateFlow(0)
val counter: StateFlow<Int> = _counter.asStateFlow()
```

### SharedFlow
```kotlin
private val _events = MutableSharedFlow<String>(
    replay = 2,
    extraBufferCapacity = 5
)
```

### Flow Operators
```kotlin
flow
    .map { it * 2 }
    .filter { it > 5 }
    .distinctUntilChanged()
    .collect { value -> 
        // Handle value
    }
```

## 🔧 Technology Stack

- **Language**: Kotlin
- **UI Framework**: Traditional Android Views (XML)
- **Architecture**: MVVM
- **Asynchronous**: Kotlin Coroutines & Flow
- **View Binding**: Enabled
- **Lifecycle**: AndroidX Lifecycle components

## 📦 Dependencies

```kotlin
implementation("androidx.core:core-ktx:1.17.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")
implementation("androidx.appcompat:appcompat:1.7.0")
implementation("com.google.android.material:material:1.12.0")
implementation("androidx.constraintlayout:constraintlayout:2.2.0")
implementation("androidx.recyclerview:recyclerview:1.3.2")
```

## 🎨 UI Components

- **MaterialCardView**: For demo sections
- **RecyclerView**: For displaying flow emissions
- **Material Design 3**: Modern UI components
- **ScrollView**: Scrollable content

## 💡 Usage Examples

### Collect StateFlow in Activity
```kotlin
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { state ->
            // Update UI with state
        }
    }
}
```

### Emit to SharedFlow
```kotlin
fun emitEvent() {
    viewModelScope.launch {
        _sharedEvents.emit("New Event")
    }
}
```

### Use Flow Operators
```kotlin
flow
    .debounce(300)
    .distinctUntilChanged()
    .flatMapLatest { query ->
        repository.search(query)
    }
    .catch { e -> emit(emptyList()) }
    .collect { results -> updateUI(results) }
```

## 🧪 Testing

The app demonstrates:
- Real-time data emission
- Operator transformations
- State management
- Event broadcasting
- Error handling
- Backpressure handling

## 📝 Code Highlights

### ViewModel State Management
```kotlin
data class FlowDemoState(
    val basicFlowData: List<String> = emptyList(),
    val stateFlowCounter: Int = 0,
    val sharedFlowData: List<String> = emptyList(),
    // ... more state
)

private val _uiState = MutableStateFlow(FlowDemoState())
val uiState: StateFlow<FlowDemoState> = _uiState.asStateFlow()
```

### Lifecycle-Aware Collection
```kotlin
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { state ->
            updateUI(state)
        }
    }
}
```

## 🎓 Learning Outcomes

After exploring this app, you will understand:
- Difference between Cold and Hot flows
- When to use Flow, StateFlow, or SharedFlow
- How to apply various Flow operators
- Best practices for Flow collection in Android
- Error handling in flows
- Backpressure management
- Lifecycle-aware flow collection

## 🤝 Contributing

Feel free to fork this project and experiment with:
- Additional Flow operators
- More complex flow combinations
- Different UI patterns
- Testing strategies

## 📄 License

This is a learning/demo project - feel free to use it for educational purposes.

## 📞 Support

For detailed explanations, refer to:
- [FLOW_CONCEPTS.md](FLOW_CONCEPTS.md) - Comprehensive Flow guide
- [Official Kotlin Flow Documentation](https://kotlinlang.org/docs/flow.html)
- [Android Developers - StateFlow and SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)

---

**Happy Learning! 🚀**

