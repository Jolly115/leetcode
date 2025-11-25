# Kotlin Flow Learning Guide

## Overview
This demo app demonstrates Kotlin Flow concepts including Cold Flows, Hot Flows (SharedFlow & StateFlow), and various Flow operators.

---

## 📚 Core Concepts

### 1. **Flow (Cold Flow)**

**What is it?**
- A cold asynchronous data stream that emits values sequentially
- "Cold" means it doesn't start emitting until collected
- Each collector gets its own independent flow

**Key Characteristics:**
- Lazy - doesn't execute until collected
- Each collector triggers a new execution
- Values are computed on-demand
- Automatically cancels when scope is cancelled

**Use Cases:**
- Database queries
- Network requests
- File reading
- Any one-time data fetch operation

**Example in App:**
```kotlin
fun createBasicFlow(): Flow<String> = flow {
    for (i in 1..5) {
        delay(500)
        emit("Basic Item $i")
    }
}
```

---

### 2. **StateFlow (Hot Flow)**

**What is it?**
- A hot flow that always has a current value
- Emits the latest value to new collectors immediately
- Only holds ONE state value at a time (latest value)

**Key Characteristics:**
- Always has a value (must be initialized)
- Conflates values (if multiple updates happen quickly, only latest matters)
- Survives configuration changes
- Multiple collectors share the same flow
- Doesn't stop emitting when there are no collectors

**Use Cases:**
- UI state management
- User session data
- App configuration
- Any state that needs to be observed by multiple components

**Example in App:**
```kotlin
private val _counter = MutableStateFlow(0)
val counter: StateFlow<Int> = _counter.asStateFlow()

fun incrementCounter() {
    _counter.value += 1
}
```

**In Activity:**
```kotlin
lifecycleScope.launch {
    viewModel.counter.collect { count ->
        tvCounter.text = count.toString()
    }
}
```

---

### 3. **SharedFlow (Hot Flow)**

**What is it?**
- A hot flow that broadcasts events to multiple collectors
- Can be configured to replay past emissions to new subscribers
- Doesn't hold a required "state" like StateFlow

**Key Characteristics:**
- Can have multiple collectors receiving the same emissions
- Configurable replay cache (replay last N values)
- Useful for events that should be broadcast
- No initial value required
- Can buffer emissions

**Configuration Parameters:**
- `replay`: Number of values to replay to new collectors
- `extraBufferCapacity`: Extra buffer capacity beyond replay
- `onBufferOverflow`: Strategy when buffer is full

**Use Cases:**
- One-time events (navigation, toasts, snackbars)
- Broadcasting notifications
- Event bus replacement
- Multi-cast scenarios

**Example in App:**
```kotlin
private val _sharedEvents = MutableSharedFlow<String>(
    replay = 2,  // Cache last 2 values
    extraBufferCapacity = 5
)
val sharedEvents: SharedFlow<String> = _sharedEvents.asSharedFlow()

fun emitSharedEvent(message: String) {
    viewModelScope.launch {
        _sharedEvents.emit("SharedFlow: $message")
    }
}
```

---

## 🔧 Flow Operators

### Transformation Operators

#### **map**
Transforms each value emitted by the flow
```kotlin
flow.map { value -> value * 2 }
```

#### **filter**
Filters values based on a predicate
```kotlin
flow.filter { it % 2 == 0 }  // Only even numbers
```

#### **transform**
More flexible than map - can emit multiple values or skip emissions
```kotlin
flow.transform { value ->
    emit("Start")
    emit(value)
    emit("End")
}
```

---

### Combination Operators

#### **combine**
Combines the latest values from multiple flows
- Emits whenever ANY flow emits
- Always uses the LATEST value from each flow
```kotlin
flow1.combine(flow2) { a, b -> "$a$b" }
```

**Example:** Search query + filter settings

#### **zip**
Pairs values from two flows
- Waits for BOTH flows to emit
- Emits pairs in order
- Completes when shortest flow completes
```kotlin
names.zip(ages) { name, age -> "$name is $age" }
```

**Example:** Pairing related data

#### **flatMapConcat**
Flattens nested flows sequentially
- Processes one inner flow at a time
- Maintains order
```kotlin
flowOf(1, 2, 3).flatMapConcat { value ->
    flow {
        emit("Start $value")
        emit("End $value")
    }
}
```

#### **flatMapMerge**
Flattens nested flows concurrently
- Processes multiple inner flows simultaneously
- May not maintain order

#### **flatMapLatest**
Cancels previous inner flow when new value arrives
- Only latest inner flow is active

---

### Filtering & Limiting Operators

#### **distinctUntilChanged**
Only emits when value changes from previous
```kotlin
flowOf(1, 1, 2, 2, 3).distinctUntilChanged()
// Emits: 1, 2, 3
```

#### **debounce**
Only emits if no new value arrives within timeout
```kotlin
searchQuery.debounce(500)  // Wait 500ms after last input
```

**Use Case:** Search as you type

#### **take**
Takes first N emissions then completes
```kotlin
flow.take(5)  // Only first 5 values
```

#### **drop**
Drops first N emissions
```kotlin
flow.drop(2)  // Skip first 2 values
```

#### **sample**
Emits the most recent value at fixed intervals
```kotlin
flow.sample(1000)  // Emit latest value every second
```

---

### Error Handling Operators

#### **catch**
Catches exceptions and provides fallback
```kotlin
flow
    .catch { e -> 
        emit(-1)  // Default value
    }
```

#### **retry**
Retries flow on exception
```kotlin
flow
    .retry(3) { cause ->
        cause is IOException  // Only retry on network errors
    }
```

#### **retryWhen**
More control over retry logic
```kotlin
flow.retryWhen { cause, attempt ->
    attempt < 3 && cause is IOException
}
```

---

### Backpressure Operators

#### **buffer**
Buffers emissions for slow collectors
```kotlin
flow
    .buffer()  // Producer can continue emitting
    .collect { 
        delay(1000)  // Slow consumer
    }
```

#### **conflate**
Skips intermediate values if collector is slow
```kotlin
flow
    .conflate()  // Only latest value matters
```

#### **collectLatest**
Cancels previous collector when new value arrives
```kotlin
flow.collectLatest { value ->
    // Cancel previous work when new value arrives
    heavyOperation(value)
}
```

---

### Context & Threading

#### **flowOn**
Changes the context of upstream flow
```kotlin
flow {
    emit(heavyComputation())
}
    .flowOn(Dispatchers.IO)  // Run on IO thread
    .collect { }  // Collect on caller's context
```

#### **shareIn**
Converts cold flow to hot SharedFlow
```kotlin
coldFlow
    .shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )
```

#### **stateIn**
Converts cold flow to hot StateFlow
```kotlin
coldFlow
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
```

---

## 🎯 Common Patterns

### 1. **Search with Debounce**
```kotlin
searchQuery
    .debounce(300)
    .distinctUntilChanged()
    .flatMapLatest { query ->
        repository.search(query)
    }
```

### 2. **Multiple Data Sources**
```kotlin
combine(
    userFlow,
    settingsFlow,
    notificationsFlow
) { user, settings, notifications ->
    UiState(user, settings, notifications)
}
```

### 3. **One-Shot Events**
```kotlin
// ViewModel
private val _events = MutableSharedFlow<Event>()
val events = _events.asSharedFlow()

// Activity
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.events.collect { event ->
            when (event) {
                is Event.ShowToast -> showToast(event.message)
                is Event.Navigate -> navigate(event.destination)
            }
        }
    }
}
```

### 4. **Loading State Management**
```kotlin
data class UiState<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

flow {
    emit(UiState(isLoading = true))
    val data = fetchData()
    emit(UiState(data = data))
}
    .catch { e ->
        emit(UiState(error = e.message))
    }
```

---

## 🔄 Cold Flow vs Hot Flow

| Aspect | Cold Flow | Hot Flow (StateFlow/SharedFlow) |
|--------|-----------|----------------------------------|
| **Execution** | On collection | Always active |
| **Collectors** | Independent streams | Share same stream |
| **Initial value** | Not required | StateFlow requires, SharedFlow optional |
| **Use case** | One-time operations | State/Events broadcasting |
| **Example** | API call | UI state |

---

## ⚡ Performance Tips

1. **Use conflate() or collectLatest()** for fast producers with slow consumers
2. **Use buffer()** when you need all emissions but at different speeds
3. **Use distinctUntilChanged()** to avoid redundant updates
4. **Use debounce()** for user input
5. **Use flowOn()** to offload heavy work to background threads
6. **Use stateIn/shareIn** to share cold flows among multiple collectors

---

## 🐛 Common Pitfalls

1. **Not using repeatOnLifecycle** in Android
   - Always collect flows lifecycle-aware in UI
   
2. **Using Flow instead of StateFlow for UI state**
   - StateFlow is better for state management
   
3. **Forgetting to handle errors**
   - Always use catch operator
   
4. **Blocking operations in flow builder**
   - Use flowOn to move to appropriate dispatcher

5. **Collecting flows in onCreate**
   - Should collect in lifecycleScope with repeatOnLifecycle

---

## 📖 Additional Resources

- [Kotlin Flows Official Guide](https://kotlinlang.org/docs/flow.html)
- [StateFlow and SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [Testing Kotlin Flows](https://developer.android.com/kotlin/flow/test)

