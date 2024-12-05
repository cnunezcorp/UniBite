package com.unibite.app.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.unibite.app.model.CartItemsModel
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CartRepository {
    private val database = FirebaseDatabase.getInstance()

    suspend fun fetchCartItems(userId: String): List<CartItemsModel> {
        val cartItems = mutableListOf<CartItemsModel>()
        val snapshot = database.reference.child("user").child(userId).child("CartItems").get().await()
        snapshot.children.forEach {
            val item = it.getValue(CartItemsModel::class.java)
            item?.let { cartItems.add(it) }
        }
        return cartItems
    }

    suspend fun addCartItem(userId: String, cartItem: CartItemsModel) {
        database.reference.child("user").child(userId).child("CartItems")
            .push().setValue(cartItem).await()
    }

    suspend fun removeCartItem(userId: String, cartItemKey: String) {
        database.reference.child("user").child(userId).child("CartItems").child(cartItemKey).removeValue().await()
    }

    suspend fun updateCartItemQuantity(userId: String, cartItemKey: String, quantity: Int) {
        database.reference.child("user").child(userId).child("CartItems")
            .child(cartItemKey).child("foodQuantity").setValue(quantity).await()
    }
}