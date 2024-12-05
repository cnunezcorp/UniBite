package com.unibite.app.viewmodel.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CartViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel() as T
    }
}


/*
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
}*/
