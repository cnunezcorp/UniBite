package com.unibite.app.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.unibite.app.model.OrderDetails
import kotlinx.coroutines.tasks.await

class OrderRepository {
    private val database = FirebaseDatabase.getInstance()

    suspend fun placeOrder(orderDetails: OrderDetails) {
        val orderReference = database.reference.child("OrderDetails").child(orderDetails.itemPushKey!!)
        orderReference.setValue(orderDetails).await()
    }

    suspend fun removeCartItems(userId: String) {
        val cartReference = database.reference.child("user").child(userId).child("CartItems")
        cartReference.removeValue().await()
    }

    suspend fun addOrderToHistory(userId: String, orderDetails: OrderDetails) {
        val historyReference = database.reference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
        historyReference.setValue(orderDetails).await()
    }
}