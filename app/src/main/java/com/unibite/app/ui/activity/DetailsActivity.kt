package com.unibite.app.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.unibite.app.R
import com.unibite.app.databinding.ActivityDetailsBinding
import com.unibite.app.model.MenuItemModel

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }
}