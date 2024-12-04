package com.unibite.app.data

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
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    suspend fun fetchCartItems(): List<CartItemsModel> = suspendCoroutine { continuation ->
        val userId = auth.currentUser?.uid ?: ""
        val cartReference = database.reference.child("user").child(userId).child("CartItems")

        cartReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = snapshot.children.mapNotNull {
                    it.getValue(CartItemsModel::class.java)
                }
                continuation.resume(cartItems)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }

    suspend fun addCartItem(cartItem: CartItemsModel) {
        val userId = auth.currentUser?.uid ?: ""
        val cartReference = database.reference.child("user").child(userId).child("CartItems")
        cartReference.push().setValue(cartItem).await()
    }

    suspend fun removeCartItem(cartItemKey: String) {
        val userId = auth.currentUser?.uid ?: ""
        val cartReference = database.reference.child("user").child(userId).child("CartItems")
        cartReference.child(cartItemKey).removeValue().await()
    }

    suspend fun updateCartItemQuantity(cartItemKey: String, newQuantity: Int) {
        val userId = auth.currentUser?.uid ?: ""
        val cartReference = database.reference.child("user").child(userId).child("CartItems")
        cartReference.child(cartItemKey).child("foodQuantity").setValue(newQuantity).await()
    }
}