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
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodDescription: String? = null
    private var foodImage: String? = null
    private var foodIngredients: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializando FirebaseAuth


        // Recibiendo el objeto completo
        val menuItem = intent.getParcelableExtra<MenuItemModel>("MenuItem")

        // Verificando y mostrando los datos
        menuItem?.let {
            with(binding) {
                detailFoodName.text = it.foodName
                detailDescriptionTextView.text = it.foodDescription
                detailIngredientsTextView.text = it.foodIngredient
                Glide.with(this@DetailsActivity).load(Uri.parse(it.foodImage)).into(detailFoodImage)
            }
        } ?: run {
            // Si no hay datos, mostrar un error o mensaje
            finish() // Opcional: Cerrar la actividad si no hay datos
        }

        // Configurando el botón de retroceso
        binding.detailBackButton.setOnClickListener {
            finish()
        }

        binding.detailAddToCartButton.setOnClickListener{
            val database = FirebaseDatabase.getInstance().reference
            val userId = auth.currentUser?.uid?:""

            //crear a cartItems Object
            val cartItem = CartItemsModel(foodName.toString(), foodPrice.toString(), foodDescription.toString(), foodImage.toString(), 1)

            //Guardando datos en la BD
            database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
                Toast.makeText(this, "Producto Añadido al Carrito Correctamente 😁", Toast.LENGTH_LONG).show()
            } .addOnFailureListener {
                Toast.makeText(this, "No Se Puede Añadir al Carrito 😥", Toast.LENGTH_SHORT).show()
            }
        }
    }
}