package com.unibite.app.domain.usecase

import com.unibite.app.data.OrderRepository
import com.unibite.app.model.OrderDetails

class OrderUseCase(private val repository: OrderRepository) {

    suspend fun placeOrder(orderDetails: OrderDetails) {
        repository.placeOrder(orderDetails)
    }

    suspend fun removeCartItems(userId: String) {
        repository.removeCartItems(userId)
    }

    suspend fun addOrderToHistory(userId: String, orderDetails: OrderDetails) {
        repository.addOrderToHistory(userId, orderDetails)
    }
}