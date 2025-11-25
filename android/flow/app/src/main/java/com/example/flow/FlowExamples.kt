package com.example.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * Additional Flow examples demonstrating various use cases and patterns
 * These are standalone examples that can be run independently for learning
 */

object FlowExamples {
    
    // ========== BASIC FLOW EXAMPLES ==========
    
    /**
     * Simple flow that emits numbers
     */
    fun simpleNumberFlow(): Flow<Int> = flow {
        for (i in 1..5) {
            delay(100)
            emit(i)
        }
    }
    
    /**
     * Flow from collection
     */
    fun flowFromCollection(): Flow<String> {
        val list = listOf("Apple", "Banana", "Cherry")
        return list.asFlow()
    }
    
    /**
     * Flow with flowOf builder
     */
    fun flowOfBuilder(): Flow<String> = flowOf("One", "Two", "Three")
    
    // ========== INTERMEDIATE OPERATORS ==========
    
    /**
     * Map operator - transforms each value
     */
    fun mapExample(): Flow<String> = flow {
        emit(1)
        emit(2)
        emit(3)
    }.map { number -> "Number: $number" }
    
    /**
     * Filter operator - filters based on predicate
     */
    fun filterExample(): Flow<Int> = flow {
        for (i in 1..10) emit(i)
    }.filter { it % 2 == 0 }  // Only even numbers
    
    /**
     * Transform operator - flexible transformation
     */
    fun transformExample(): Flow<String> = flow {
        emit(1)
        emit(2)
    }.transform { value ->
        emit("Start")
        emit("Value: $value")
        emit("End")
    }
    
    // ========== TERMINAL OPERATORS ==========
    
    /**
     * Demonstrates various terminal operators
     */
    suspend fun terminalOperatorsExample() {
        val flow = flowOf(1, 2, 3, 4, 5)
        
        // collect - collects all values
        flow.collect { println(it) }
        
        // toList - converts to list
        val list = flow.toList()
        
        // first - gets first value
        val first = flow.first()
        
        // last - gets last value
        val last = flow.last()
        
        // reduce - reduces to single value
        val sum = flow.reduce { acc, value -> acc + value }
        
        // fold - like reduce but with initial value
        val product = flow.fold(1) { acc, value -> acc * value }
    }
    
    // ========== COMBINATION OPERATORS ==========
    
    /**
     * Combine - combines latest values from multiple flows
     */
    fun combineExample(): Flow<String> {
        val names = flowOf("Alice", "Bob", "Charlie")
        val ages = flowOf(25, 30, 35)
        
        return names.combine(ages) { name, age ->
            "$name is $age years old"
        }
    }
    
    /**
     * Zip - pairs corresponding values
     */
    fun zipExample(): Flow<String> {
        val flow1 = flowOf("A", "B", "C")
        val flow2 = flowOf(1, 2, 3)
        
        return flow1.zip(flow2) { letter, number ->
            "$letter$number"
        }
    }
    
    /**
     * Merge - merges multiple flows
     */
    fun mergeExample(): Flow<Int> {
        val flow1 = flowOf(1, 2, 3)
        val flow2 = flowOf(4, 5, 6)
        
        return merge(flow1, flow2)
    }
    
    // ========== FLATTENING OPERATORS ==========
    
    /**
     * flatMapConcat - processes flows sequentially
     */
    fun flatMapConcatExample(): Flow<String> = flowOf(1, 2, 3)
        .flatMapConcat { value ->
            flow {
                emit("Start $value")
                delay(100)
                emit("End $value")
            }
        }
    
    /**
     * flatMapMerge - processes flows concurrently
     */
    fun flatMapMergeExample(): Flow<String> = flowOf(1, 2, 3)
        .flatMapMerge { value ->
            flow {
                emit("Processing $value")
                delay(100)
                emit("Done $value")
            }
        }
    
    /**
     * flatMapLatest - cancels previous when new value arrives
     */
    fun flatMapLatestExample(): Flow<String> = flowOf("a", "b", "c")
        .onEach { delay(100) }
        .flatMapLatest { value ->
            flow {
                emit("Start $value")
                delay(200)
                emit("End $value")  // May be cancelled
            }
        }
    
    // ========== SIZE LIMITING OPERATORS ==========
    
    /**
     * Take - takes first N elements
     */
    fun takeExample(): Flow<Int> = flowOf(1, 2, 3, 4, 5)
        .take(3)  // Takes 1, 2, 3
    
    /**
     * TakeWhile - takes while condition is true
     */
    fun takeWhileExample(): Flow<Int> = flowOf(1, 2, 3, 4, 5)
        .takeWhile { it < 4 }  // Takes 1, 2, 3
    
    /**
     * Drop - drops first N elements
     */
    fun dropExample(): Flow<Int> = flowOf(1, 2, 3, 4, 5)
        .drop(2)  // Emits 3, 4, 5
    
    // ========== EXCEPTION HANDLING ==========
    
    /**
     * Catch operator for exception handling
     */
    fun catchExample(): Flow<Int> = flow {
        emit(1)
        emit(2)
        throw RuntimeException("Error!")
        emit(3)  // Never reached
    }.catch { e ->
        println("Caught exception: ${e.message}")
        emit(-1)  // Emit fallback value
    }
    
    /**
     * Retry operator
     */
    fun retryExample(): Flow<Int> = flow {
        var attempt = 0
        while (true) {
            emit(attempt)
            if (attempt < 2) {
                attempt++
                throw RuntimeException("Retry $attempt")
            }
            break
        }
    }.retry(3) { cause ->
        println("Retrying due to: ${cause.message}")
        true  // Retry on any exception
    }
    
    /**
     * OnCompletion - executes when flow completes or fails
     */
    fun onCompletionExample(): Flow<Int> = flowOf(1, 2, 3)
        .onCompletion { cause ->
            if (cause != null) {
                println("Flow completed with exception: $cause")
            } else {
                println("Flow completed successfully")
            }
        }
    
    // ========== CONTEXT PRESERVATION ==========
    
    /**
     * FlowOn - changes the context of flow execution
     */
    fun flowOnExample(): Flow<Int> = flow {
        println("Flow on thread: ${Thread.currentThread().name}")
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }
        // .flowOn(Dispatchers.IO)  // Uncomment to run on IO dispatcher
    
    // ========== BUFFERING ==========
    
    /**
     * Buffer - buffers emissions
     */
    fun bufferExample(): Flow<Int> = flow {
        for (i in 1..5) {
            delay(100)  // Fast producer
            emit(i)
        }
    }.buffer()  // Buffer emissions
    
    /**
     * Conflate - only latest value matters
     */
    fun conflateExample(): Flow<Int> = flow {
        for (i in 1..10) {
            delay(50)  // Fast producer
            emit(i)
        }
    }.conflate()  // Skip intermediate values
    
    // ========== DISTINCT OPERATORS ==========
    
    /**
     * DistinctUntilChanged - emits only when value changes
     */
    fun distinctUntilChangedExample(): Flow<Int> = flowOf(1, 1, 2, 2, 2, 3, 3, 1)
        .distinctUntilChanged()  // Emits: 1, 2, 3, 1
    
    /**
     * DistinctUntilChangedBy - uses selector for comparison
     */
    fun distinctUntilChangedByExample(): Flow<String> = 
        flowOf("apple", "apricot", "banana", "blueberry")
            .distinctUntilChangedBy { it.first() }  // Compare by first letter
    
    // ========== DELAY OPERATORS ==========
    
    /**
     * Debounce - waits for timeout before emitting
     */
    fun debounceExample(): Flow<String> = flow {
        emit("a")
        delay(100)
        emit("b")
        delay(100)
        emit("c")
        delay(600)  // Long delay
        emit("d")
    }.debounce(200)  // Only emits 'c' and 'd'
    
    /**
     * Sample - emits at periodic intervals
     */
    fun sampleExample(): Flow<Int> = flow {
        for (i in 1..100) {
            delay(50)
            emit(i)
        }
    }.sample(200)  // Emits every 200ms
    
    // ========== STATE MANAGEMENT ==========
    
    /**
     * StateIn - converts cold flow to StateFlow
     */
    // fun stateInExample(scope: CoroutineScope): StateFlow<Int> = 
    //     flow {
    //         var count = 0
    //         while (true) {
    //             emit(count++)
    //             delay(1000)
    //         }
    //     }.stateIn(
    //         scope = scope,
    //         started = SharingStarted.WhileSubscribed(5000),
    //         initialValue = 0
    //     )
    
    /**
     * ShareIn - converts cold flow to SharedFlow
     */
    // fun shareInExample(scope: CoroutineScope): SharedFlow<Int> = 
    //     flow {
    //         var count = 0
    //         while (true) {
    //             emit(count++)
    //             delay(1000)
    //         }
    //     }.shareIn(
    //         scope = scope,
    //         started = SharingStarted.WhileSubscribed(5000),
    //         replay = 1
    //     )
    
    // ========== PRACTICAL EXAMPLES ==========
    
    /**
     * Search with debounce pattern
     */
    fun searchFlow(query: Flow<String>): Flow<List<String>> = query
        .debounce(300)  // Wait for user to stop typing
        .distinctUntilChanged()  // Skip if same as previous
        .filter { it.length >= 3 }  // Minimum 3 characters
        .flatMapLatest { searchQuery ->
            flow {
                // Simulate API call
                delay(500)
                emit(listOf("Result 1", "Result 2", "Result 3"))
            }
        }
        .catch { e ->
            println("Search error: ${e.message}")
            emit(emptyList())
        }
    
    /**
     * Pagination pattern
     */
    fun paginationFlow(pageFlow: Flow<Int>): Flow<List<String>> = pageFlow
        .flatMapConcat { page ->
            flow {
                // Simulate loading page
                delay(500)
                val items = (1..10).map { "Page $page Item $it" }
                emit(items)
            }
        }
    
    /**
     * Polling pattern
     */
    fun pollingFlow(intervalMs: Long): Flow<Long> = flow {
        var count = 0L
        while (true) {
            emit(count++)
            delay(intervalMs)
        }
    }
    
    /**
     * Exponential backoff retry
     */
    fun exponentialBackoffFlow(): Flow<String> = flow {
        var attempt = 0
        while (true) {
            if (attempt < 3) {
                attempt++
                throw RuntimeException("Attempt $attempt failed")
            }
            emit("Success!")
            break
        }
    }.retryWhen { cause, attempt ->
        if (attempt < 3) {
            val delayTime = (100 * (attempt + 1)).toLong()
            delay(delayTime)
            true
        } else {
            false
        }
    }
}

/**
 * Example usage - uncomment to test
 */
fun main() = runBlocking {
    println("=== Simple Flow ===")
    FlowExamples.simpleNumberFlow().collect { println(it) }
    
    println("\n=== Map Example ===")
    FlowExamples.mapExample().collect { println(it) }
    
    println("\n=== Filter Example ===")
    FlowExamples.filterExample().collect { println(it) }
    
    println("\n=== Combine Example ===")
    FlowExamples.combineExample().collect { println(it) }
    
    println("\n=== Zip Example ===")
    FlowExamples.zipExample().collect { println(it) }
    
    println("\n=== FlatMapConcat Example ===")
    FlowExamples.flatMapConcatExample().collect { println(it) }
    
    println("\n=== Catch Example ===")
    FlowExamples.catchExample().collect { println(it) }
}

