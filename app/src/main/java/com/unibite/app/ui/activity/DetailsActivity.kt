package com.unibite.app.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.unibite.app.R
import com.unibite.app.databinding.ActivityDetailsBinding
import com.unibite.app.model.CartItemsModel
import com.unibite.app.model.MenuItemModel

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var auth: FirebaseAuth
    private var menuItem: MenuItemModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializando FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Recibiendo el objeto completo
        menuItem = intent.getParcelableExtra("MenuItem")

        // Verificando y mostrando los datos
        menuItem?.let { item ->
            with(binding) {
                detailFoodName.text = item.foodName
                detailDescriptionTextView.text = item.foodDescription
                detailIngredientsTextView.text = item.foodIngredient
                Glide.with(this@DetailsActivity).load(Uri.parse(item.foodImage)).into(detailFoodImage)
            }
        } ?: run {
            finish()
        }

        binding.detailBackButton.setOnClickListener {
            finish()
        }

        binding.detailAddToCartButton.setOnClickListener {
            addToCart()
        }
    }

    private fun addToCart() {
        menuItem?.let { item ->
            val database = FirebaseDatabase.getInstance().reference
            val userId = auth.currentUser?.uid ?: ""

            // Referencia al nodo CartItems del usuario
            val cartReference = database.child("user").child(userId).child("CartItems")

            // Verifica si el producto ya existe en el carrito
            cartReference.orderByChild("foodName").equalTo(item.foodName).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        Toast.makeText(this, "El producto ya está en el carrito", Toast.LENGTH_SHORT).show()
                    } else {
                        // Si no existe, añadirlo
                        val cartItem = CartItemsModel(
                            foodName = item.foodName,
                            foodPrice = item.foodPrice,
                            foodDescription = item.foodDescription,
                            foodImage = item.foodImage,
                            foodQuantity = 1,
                            foodIngredient = item.foodIngredient
                        )

                        cartReference.push().setValue(cartItem)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Producto añadido al carrito correctamente 😁", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "No se puede añadir al carrito 😥", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {exception ->
                    Log.e("DetailsActivity", "Error al verificar el carrito: ${exception.message}")
                    Toast.makeText(this, "Error al verificar el carrito", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(this, "Error: Información del producto no disponible", Toast.LENGTH_SHORT).show()
        }
    }
}