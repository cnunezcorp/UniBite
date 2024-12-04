package com.unibite.app.domain.usecase

import com.unibite.app.data.CartRepository
import com.unibite.app.model.CartItemsModel

class CartUseCase(private val repository: CartRepository) {
    suspend fun fetchCartItems() = repository.fetchCartItems()
    suspend fun addCartItem(cartItem: CartItemsModel) = repository.addCartItem(cartItem)
    suspend fun removeCartItem(cartItemKey: String) = repository.removeCartItem(cartItemKey)
    suspend fun updateCartItemQuantity(cartItemKey: String, newQuantity: Int) =
        repository.updateCartItemQuantity(cartItemKey, newQuantity)
}