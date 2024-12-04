package com.unibite.app.ui.activity

import android.net.Uri
import android.os.Bundle
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
            // Si no hay datos, mostrar un error o mensaje
            finish() // Opcional: Cerrar la actividad si no hay datos
        }

        // Configurando el botón de retroceso
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

            // Crear un CartItemsModel con todos los valores del MenuItem
            val cartItem = CartItemsModel(
                foodName = item.foodName,
                foodPrice = item.foodPrice,
                foodDescription = item.foodDescription,
                foodImage = item.foodImage,
                foodQuantity = 1,
                foodIngredient = item.foodIngredient
            )

            // Guardando datos en la BD
            database.child("user").child(userId).child("CartItems").push().setValue(cartItem)
                .addOnSuccessListener {
                    Toast.makeText(this, "Producto Añadido al Carrito Correctamente 😁", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "No Se Puede Añadir al Carrito 😥", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(this, "Error: Información del producto no disponible", Toast.LENGTH_SHORT).show()
        }
    }
}