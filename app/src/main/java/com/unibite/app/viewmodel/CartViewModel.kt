package com.unibite.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.unibite.app.model.CartItemsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItemsModel>>(emptyList())
    val cartItems: StateFlow<List<CartItemsModel>> get() = _cartItems

    fun fetchCartItems() {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
                val databaseReference = FirebaseDatabase.getInstance().getReference("user").child(userId).child("CartItems")

                databaseReference.get().addOnSuccessListener { snapshot ->
                    val cartItems = mutableListOf<CartItemsModel>()
                    snapshot.children.forEach { child ->
                        val item = child.getValue(CartItemsModel::class.java)
                        item?.let { cartItems.add(it) }
                    }
                    _cartItems.value = cartItems
                }.addOnFailureListener { exception ->
                    Log.e("CartViewModel", "Error al obtener los datos: ${exception.message}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error en fetchCartItems: ${e.message}")
            }
        }
    }

    fun removeCartItem(itemKey: String) {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
                val databaseReference = FirebaseDatabase.getInstance().getReference("user").child(userId).child("CartItems")
                databaseReference.child(itemKey).removeValue().addOnSuccessListener {
                    fetchCartItems() // Actualiza la lista después de eliminar el ítem
                }.addOnFailureListener { exception ->
                    Log.e("CartViewModel", "Error al eliminar el ítem: ${exception.message}")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error en removeCartItemFromFirebase: ${e.message}")
            }
        }
    }

    fun updateCartItemQuantity(foodName: String, quantity: Int) {
        viewModelScope.launch {
            _cartItems.value = _cartItems.value.map {
                if (it.foodName == foodName) it.copy(foodQuantity = quantity) else it
            }
        }
    }
}
