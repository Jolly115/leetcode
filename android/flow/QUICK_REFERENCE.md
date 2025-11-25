# Kotlin Flow Quick Reference Guide

## 📋 Quick Comparison

### Cold Flow vs Hot Flow

```kotlin
// COLD FLOW - starts on collection
val coldFlow = flow {
    emit(1)
    emit(2)
}
// Each collector gets its own flow

// HOT FLOW - StateFlow
val stateFlow = MutableStateFlow(0)
// Always has value, multiple collectors share

// HOT FLOW - SharedFlow  
val sharedFlow = MutableSharedFlow<String>()
// Can broadcast events, configurable replay
```

---

## 🎯 When to Use What?

| Use Case | Use This |
|----------|----------|
| UI State | `StateFlow` |
| One-time Events | `SharedFlow` |
| Database Query | Cold `Flow` |
| Network Request | Cold `Flow` |
| User Input Stream | Cold `Flow` |
| Broadcasting | `SharedFlow` |
| Configuration | `StateFlow` |

---

## 🔧 Common Operators Cheat Sheet

### Transformation
```kotlin
.map { it * 2 }                    // Transform each value
.filter { it > 0 }                 // Filter by condition
.transform { emit(it); emit(it) }  // Flexible transformation
```

### Combination
```kotlin
.combine(other) { a, b -> a + b }  // Latest from both
.zip(other) { a, b -> a to b }     // Pair in order
```

### Flattening
```kotlin
.flatMapConcat { ... }  // Sequential
.flatMapMerge { ... }   // Concurrent
.flatMapLatest { ... }  // Cancel previous
```

### Filtering
```kotlin
.distinctUntilChanged()  // Only when changed
.debounce(300)          // Wait 300ms
.take(5)                // First 5 items
.drop(2)                // Skip first 2
.filter { it > 0 }      // Conditional
```

### Error Handling
```kotlin
.catch { emit(default) }     // Handle errors
.retry(3)                    // Retry 3 times
.onCompletion { }            // On complete
```

### Backpressure
```kotlin
.buffer()      // Buffer emissions
.conflate()    // Skip intermediate
```

### Context
```kotlin
.flowOn(Dispatchers.IO)  // Run on IO thread
```

---

## 📱 Android Lifecycle Integration

### Correct Way to Collect in Activity/Fragment
```kotlin
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.stateFlow.collect { state ->
            // Update UI
        }
    }
}
```

### ❌ WRONG - Don't do this
```kotlin
// DON'T: Collect in onCreate without lifecycle awareness
lifecycleScope.launch {
    viewModel.flow.collect { }  // Leaks!
}
```

---

## 🏗️ ViewModel Patterns

### StateFlow for UI State
```kotlin
class MyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun updateState() {
        _uiState.update { it.copy(data = newData) }
    }
}
```

### SharedFlow for Events
```kotlin
class MyViewModel : ViewModel() {
    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()
    
    fun sendEvent() {
        viewModelScope.launch {
            _events.emit(Event.ShowToast("Hello"))
        }
    }
}
```

### Cold Flow for Data
```kotlin
class MyViewModel : ViewModel() {
    fun getData(): Flow<Data> = repository.getData()
        .flowOn(Dispatchers.IO)
}
```

---

## 💡 Common Patterns

### Search with Debounce
```kotlin
searchQueryFlow
    .debounce(300)
    .distinctUntilChanged()
    .filter { it.length >= 3 }
    .flatMapLatest { query ->
        repository.search(query)
    }
    .catch { emit(emptyList()) }
```

### Loading State
```kotlin
flow {
    emit(Loading)
    val data = fetchData()
    emit(Success(data))
}
    .catch { emit(Error(it)) }
    .stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = Initial
    )
```

### Combine Multiple Sources
```kotlin
combine(
    userFlow,
    settingsFlow,
    notificationsFlow
) { user, settings, notifications ->
    UiState(user, settings, notifications)
}
```

### Retry with Exponential Backoff
```kotlin
flow { fetchData() }
    .retryWhen { cause, attempt ->
        if (attempt < 3 && cause is IOException) {
            delay(100 * (attempt + 1))
            true
        } else false
    }
```

---

## ⚡ Performance Tips

1. **Use `conflate()` or `collectLatest()`** for UI updates
```kotlin
flow.conflate().collect { updateUI(it) }
// or
flow.collectLatest { updateUI(it) }
```

2. **Buffer for different speeds**
```kotlin
flow.buffer().collect { slowOperation(it) }
```

3. **Use `flowOn()` for background work**
```kotlin
flow { heavyWork() }
    .flowOn(Dispatchers.IO)
    .collect { updateUI(it) }
```

4. **Share cold flows**
```kotlin
val sharedFlow = coldFlow
    .shareIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        replay = 1
    )
```

---

## 🐛 Common Mistakes

### ❌ Mistake #1: Blocking operations
```kotlin
// DON'T
flow {
    val data = blockingNetworkCall()  // Blocks!
    emit(data)
}

// DO
flow {
    val data = suspendingNetworkCall()  // Suspends!
    emit(data)
}
```

### ❌ Mistake #2: Not using lifecycle
```kotlin
// DON'T - in Activity
lifecycleScope.launch {
    viewModel.flow.collect { }  // Keeps collecting when stopped
}

// DO
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.flow.collect { }  // Stops when lifecycle stops
    }
}
```

### ❌ Mistake #3: Wrong flow type
```kotlin
// DON'T - using Flow for state
val state: Flow<State> = flow { ... }

// DO - use StateFlow
val state: StateFlow<State> = MutableStateFlow(initialState)
```

### ❌ Mistake #4: No error handling
```kotlin
// DON'T
flow { riskyOperation() }.collect { }

// DO
flow { riskyOperation() }
    .catch { e -> handleError(e) }
    .collect { }
```

---

## 📊 Operator Selection Guide

### Need to transform values?
- Simple transform → `map`
- Complex transform → `transform`
- Async transform → `flatMapConcat/Merge/Latest`

### Need to filter?
- By condition → `filter`
- By uniqueness → `distinctUntilChanged`
- By count → `take`, `drop`
- By time → `debounce`, `sample`

### Need to combine flows?
- Latest values → `combine`
- Pair values → `zip`
- Merge all → `merge`

### Have slow consumer?
- Need all values → `buffer`
- Only latest matters → `conflate` or `collectLatest`

### Need error handling?
- Provide fallback → `catch`
- Retry → `retry` or `retryWhen`

---

## 🎓 Testing Flows

```kotlin
@Test
fun testFlow() = runTest {
    val flow = flowOf(1, 2, 3)
    val result = flow.toList()
    assertEquals(listOf(1, 2, 3), result)
}

@Test
fun testStateFlow() = runTest {
    val stateFlow = MutableStateFlow(0)
    assertEquals(0, stateFlow.value)
    
    stateFlow.value = 1
    assertEquals(1, stateFlow.value)
}

@Test
fun testCollect() = runTest {
    val values = mutableListOf<Int>()
    flowOf(1, 2, 3).collect { values.add(it) }
    assertEquals(listOf(1, 2, 3), values)
}
```

---

## 🔗 Quick Links

- [Official Docs](https://kotlinlang.org/docs/flow.html)
- [Android Guide](https://developer.android.com/kotlin/flow)
- [StateFlow & SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)

---

## 📞 Remember

✅ **Cold Flow** = New stream per collector  
✅ **StateFlow** = Current state holder  
✅ **SharedFlow** = Event broadcaster  
✅ Always use `repeatOnLifecycle` in Android UI  
✅ Use `flowOn` for background work  
✅ Handle errors with `catch`  
✅ Test your flows!

---

**Print this and keep it handy! 📄**

