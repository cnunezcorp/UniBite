package com.unibite.app.domain.usecase

import com.unibite.app.data.CartRepository
import com.unibite.app.model.CartItemsModel

class CartUseCase(private val repository: CartRepository) {

    suspend fun fetchCartItems(userId: String): List<CartItemsModel> {
        return repository.fetchCartItems(userId)
    }

    suspend fun addCartItem(userId: String, cartItem: CartItemsModel) {
        repository.addCartItem(userId, cartItem)
    }

    suspend fun removeCartItem(userId: String, cartItemKey: String) {
        repository.removeCartItem(userId, cartItemKey)
    }

    suspend fun updateCartItemQuantity(userId: String, cartItemKey: String, quantity: Int) {
        repository.updateCartItemQuantity(userId, cartItemKey, quantity)
    }
}