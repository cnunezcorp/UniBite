package com.unibite.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibite.app.domain.usecase.CartUseCase
import com.unibite.app.model.CartItemsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val useCase: CartUseCase) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItemsModel>>(emptyList())
    val cartItems: StateFlow<List<CartItemsModel>> get() = _cartItems

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun fetchCartItems() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _cartItems.value = useCase.fetchCartItems()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addCartItem(cartItem: CartItemsModel) {
        viewModelScope.launch {
            try {
                useCase.addCartItem(cartItem)
                fetchCartItems()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun removeCartItem(cartItemKey: String) {
        viewModelScope.launch {
            try {
                useCase.removeCartItem(cartItemKey)
                fetchCartItems()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateCartItemQuantity(cartItemKey: String, newQuantity: Int) {
        viewModelScope.launch {
            try {
                useCase.updateCartItemQuantity(cartItemKey, newQuantity)
                fetchCartItems()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}