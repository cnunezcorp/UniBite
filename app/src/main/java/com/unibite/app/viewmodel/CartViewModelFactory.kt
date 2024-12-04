package com.unibite.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unibite.app.data.CartRepository
import com.unibite.app.domain.usecase.CartUseCase

class CartViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            val repository = CartRepository()
            val useCase = CartUseCase(repository)
            return CartViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}