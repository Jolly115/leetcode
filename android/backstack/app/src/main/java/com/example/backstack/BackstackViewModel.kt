package com.example.backstack

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BackstackViewModel : ViewModel() {
    private val _backstackState = MutableStateFlow<List<String>>(listOf("Home"))
    val backstackState: StateFlow<List<String>> = _backstackState.asStateFlow()

    fun navigateTo(screenName: String) {
        val currentStack = _backstackState.value.toMutableList()
        currentStack.add(screenName)
        _backstackState.value = currentStack
    }
    
    fun navigateToWithPopUpTo(screenName: String, popUpTo: String, inclusive: Boolean) {
        val currentStack = _backstackState.value.toMutableList()
        val popIndex = currentStack.indexOfLast { it == popUpTo }
        if (popIndex != -1) {
            val removeUntil = if (inclusive) popIndex else popIndex + 1
            currentStack.subList(removeUntil, currentStack.size).clear()
        }
        if (currentStack.lastOrNull() != screenName) {
            currentStack.add(screenName)
        }
        _backstackState.value = currentStack
    }
    
    fun navigateToWithSingleTop(screenName: String) {
        val currentStack = _backstackState.value.toMutableList()
        if (currentStack.lastOrNull() != screenName) {
            currentStack.add(screenName)
            _backstackState.value = currentStack
        }
    }
    
    fun popBackStack() {
        val currentStack = _backstackState.value.toMutableList()
        if (currentStack.size > 1) {
            currentStack.removeAt(currentStack.size - 1)
            _backstackState.value = currentStack
        }
    }
    
    fun getCurrentScreen(): String {
        return _backstackState.value.lastOrNull() ?: "Home"
    }
}

