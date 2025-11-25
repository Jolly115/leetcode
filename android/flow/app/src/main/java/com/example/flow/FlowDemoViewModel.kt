package com.example.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FlowDemoState(
    val basicFlowData: List<String> = emptyList(),
    val transformedData: List<String> = emptyList(),
    val filteredData: List<String> = emptyList(),
    val combinedData: String = "",
    val sharedFlowData: List<String> = emptyList(),
    val stateFlowCounter: Int = 0,
    val debounceData: String = "",
    val distinctData: List<String> = emptyList(),
    val flatMapData: List<String> = emptyList(),
    val zipData: List<String> = emptyList()
)

class FlowDemoViewModel : ViewModel() {
    
    // StateFlow - Hot flow that always has a value and emits latest to new collectors
    private val _uiState = MutableStateFlow(FlowDemoState())
    val uiState: StateFlow<FlowDemoState> = _uiState.asStateFlow()
    
    // SharedFlow - Hot flow that can emit to multiple collectors
    private val _sharedEvents = MutableSharedFlow<String>(
        replay = 2, // Cache last 2 values for new subscribers
        extraBufferCapacity = 5
    )
    val sharedEvents: SharedFlow<String> = _sharedEvents.asSharedFlow()
    
    // Counter StateFlow for demonstration
    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()
    
    init {
        // Collect shared events
        viewModelScope.launch {
            sharedEvents.collect { event ->
                val current = _uiState.value.sharedFlowData.toMutableList()
                current.add(event)
                _uiState.update { it.copy(sharedFlowData = current) }
            }
        }
        
        // Collect counter updates
        viewModelScope.launch {
            counter.collect { count ->
                _uiState.update { it.copy(stateFlowCounter = count) }
            }
        }
    }
    
    // ========== BASIC FLOW DEMOS ==========
    
    /**
     * Creates a basic cold flow that emits items one by one
     * Cold flows start emitting when collected and each collector gets its own flow
     */
    fun demoBasicFlow() {
        viewModelScope.launch {
            createBasicFlow()
                .collect { value ->
                    val current = _uiState.value.basicFlowData.toMutableList()
                    current.add(value)
                    _uiState.update { it.copy(basicFlowData = current) }
                }
        }
    }
    
    private fun createBasicFlow(): Flow<String> = flow {
        for (i in 1..5) {
            delay(500) // Simulate network/database call
            emit("Basic Item $i")
        }
    }
    
    // ========== FLOW OPERATORS DEMOS ==========
    
    /**
     * Demonstrates map, filter, and transform operators
     */
    fun demoTransformOperators() {
        viewModelScope.launch {
            // Map operator - transforms each value
            createNumberFlow()
                .map { "Mapped: $it -> ${it * 2}" }
                .collect { value ->
                    val current = _uiState.value.transformedData.toMutableList()
                    current.add(value)
                    _uiState.update { it.copy(transformedData = current) }
                }
        }
        
        viewModelScope.launch {
            // Filter operator - filters values based on condition
            createNumberFlow()
                .filter { it % 2 == 0 }
                .map { "Filtered (Even): $it" }
                .collect { value ->
                    val current = _uiState.value.filteredData.toMutableList()
                    current.add(value)
                    _uiState.update { it.copy(filteredData = current) }
                }
        }
    }
    
    private fun createNumberFlow(): Flow<Int> = flow {
        for (i in 1..10) {
            delay(300)
            emit(i)
        }
    }
    
    /**
     * Demonstrates combine operator - combines multiple flows
     */
    fun demoCombineOperator() {
        viewModelScope.launch {
            val flow1 = flowOf("A", "B", "C").onEach { delay(100) }
            val flow2 = flowOf(1, 2, 3).onEach { delay(150) }
            
            flow1.combine(flow2) { str, num ->
                "$str$num"
            }.collect { combined ->
                _uiState.update { it.copy(combinedData = combined) }
                delay(500)
            }
        }
    }
    
    /**
     * Demonstrates zip operator - pairs values from two flows
     */
    fun demoZipOperator() {
        viewModelScope.launch {
            val names = flowOf("Alice", "Bob", "Charlie").onEach { delay(200) }
            val ages = flowOf(25, 30, 35).onEach { delay(300) }
            
            names.zip(ages) { name, age ->
                "$name is $age years old"
            }.collect { result ->
                val current = _uiState.value.zipData.toMutableList()
                current.add(result)
                _uiState.update { it.copy(zipData = current) }
            }
        }
    }
    
    /**
     * Demonstrates flatMapConcat - flattens nested flows sequentially
     */
    fun demoFlatMapOperator() {
        viewModelScope.launch {
            flowOf(1, 2, 3)
                .onEach { delay(200) }
                .flatMapConcat { value ->
                    flow {
                        emit("Start $value")
                        delay(300)
                        emit("End $value")
                    }
                }
                .collect { result ->
                    val current = _uiState.value.flatMapData.toMutableList()
                    current.add(result)
                    _uiState.update { it.copy(flatMapData = current) }
                }
        }
    }
    
    /**
     * Demonstrates debounce operator - only emits after a timeout
     */
    fun demoDebounceOperator(searchQuery: String) {
        viewModelScope.launch {
            flowOf(searchQuery)
                .debounce(500) // Wait 500ms after last emission
                .collect { query ->
                    _uiState.update { it.copy(debounceData = "Searched: $query") }
                }
        }
    }
    
    /**
     * Demonstrates distinctUntilChanged - only emits when value changes
     */
    fun demoDistinctOperator() {
        viewModelScope.launch {
            flowOf(1, 1, 2, 2, 2, 3, 3, 1, 1, 4)
                .onEach { delay(200) }
                .distinctUntilChanged()
                .map { "Distinct: $it" }
                .collect { value ->
                    val current = _uiState.value.distinctData.toMutableList()
                    current.add(value)
                    _uiState.update { it.copy(distinctData = current) }
                }
        }
    }
    
    // ========== SHARED FLOW DEMOS ==========
    
    /**
     * SharedFlow - Hot flow that broadcasts to all collectors
     * Multiple collectors will receive the same emissions
     */
    fun emitSharedEvent(message: String) {
        viewModelScope.launch {
            _sharedEvents.emit("SharedFlow: $message")
        }
    }
    
    // ========== STATE FLOW DEMOS ==========
    
    /**
     * StateFlow - Holds a single state value and emits updates
     * Always has a value, conflates emissions (only latest matters)
     */
    fun incrementCounter() {
        _counter.value += 1
    }
    
    fun decrementCounter() {
        _counter.value -= 1
    }
    
    fun resetCounter() {
        _counter.value = 0
    }
    
    // ========== ADVANCED OPERATORS ==========
    
    /**
     * Demonstrates take, drop, and other limiting operators
     */
    fun demoLimitingOperators() {
        viewModelScope.launch {
            createNumberFlow()
                .take(5) // Take only first 5 emissions
                .collect { value ->
                    val current = _uiState.value.basicFlowData.toMutableList()
                    current.add("Take(5): $value")
                    _uiState.update { it.copy(basicFlowData = current) }
                }
        }
    }
    
    /**
     * Demonstrates retry and catch for error handling
     */
    fun demoErrorHandling() {
        viewModelScope.launch {
            flow {
                for (i in 1..5) {
                    if (i == 3) {
                        throw Exception("Error at $i")
                    }
                    emit(i)
                }
            }
                .retry(2) { cause ->
                    // Retry on exception
                    true
                }
                .catch { e ->
                    emit(-1) // Emit default value on error
                }
                .collect { value ->
                    val current = _uiState.value.basicFlowData.toMutableList()
                    current.add("Error Handling: $value")
                    _uiState.update { it.copy(basicFlowData = current) }
                }
        }
    }
    
    /**
     * Demonstrates buffer operator for handling backpressure
     */
    fun demoBufferOperator() {
        viewModelScope.launch {
            flow {
                repeat(5) {
                    delay(100) // Fast producer
                    emit(it)
                }
            }
                .buffer() // Buffer emissions
                .collect { value ->
                    delay(500) // Slow consumer
                    val current = _uiState.value.basicFlowData.toMutableList()
                    current.add("Buffered: $value")
                    _uiState.update { it.copy(basicFlowData = current) }
                }
        }
    }
    
    // ========== RESET FUNCTIONS ==========
    
    fun clearBasicFlow() {
        _uiState.update { it.copy(basicFlowData = emptyList()) }
    }
    
    fun clearTransformedData() {
        _uiState.update { it.copy(transformedData = emptyList()) }
    }
    
    fun clearFilteredData() {
        _uiState.update { it.copy(filteredData = emptyList()) }
    }
    
    fun clearSharedFlow() {
        _uiState.update { it.copy(sharedFlowData = emptyList()) }
    }
    
    fun clearAllData() {
        _uiState.value = FlowDemoState()
        _counter.value = 0
    }
}

