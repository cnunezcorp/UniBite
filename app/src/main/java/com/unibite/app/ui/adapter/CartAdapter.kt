package com.unibite.app.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.unibite.app.databinding.CartItemBinding

class CartAdapter(
    private val context: Context,
    private val onRemoveItem: (String) -> Unit,
    private val onUpdateQuantity: (String, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val foodNames = mutableListOf<String>()
    private val foodPrices = mutableListOf<String>()
    private val foodDescriptions = mutableListOf<String>()
    private val foodImages = mutableListOf<String>()
    private val foodQuantities = mutableListOf<Int>()
    private val foodIngredients = mutableListOf<String>()
    private val itemKeys = mutableListOf<String>()

    fun updateItems(
        names: List<String>,
        prices: List<String>,
        descriptions: List<String>,
        images: List<String>,
        quantities: List<Int>,
        ingredients: List<String>,
        keys: List<String>
    ) {
        foodNames.clear()
        foodPrices.clear()
        foodDescriptions.clear()
        foodImages.clear()
        foodQuantities.clear()
        foodIngredients.clear()
        itemKeys.clear()

        foodNames.addAll(names)
        foodPrices.addAll(prices)
        foodDescriptions.addAll(descriptions)
        foodImages.addAll(images)
        foodQuantities.addAll(quantities)
        foodIngredients.addAll(ingredients)
        itemKeys.addAll(keys)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodNames.size

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                cartFoodName.text = foodNames[position]
                cartItemPrice.text = "${foodPrices[position]}$"

                Glide.with(context).load(foodImages[position]).into(cartFoodImage)

                cartItemQuantity.text = foodQuantities[position].toString()

                minusButton.setOnClickListener {
                    if (foodQuantities[position] > 1) {
                        onUpdateQuantity(foodNames[position], foodQuantities[position] - 1)
                    }
                }

                plusButton.setOnClickListener {
                    onUpdateQuantity(foodNames[position], foodQuantities[position] + 1)
                }

                deleteButton.setOnClickListener {
                    val itemKey = itemKeys[position]
                    onRemoveItem(itemKey)
                }
            }
        }
    }
}
