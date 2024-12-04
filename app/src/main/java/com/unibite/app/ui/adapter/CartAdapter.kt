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

    fun updateItems(
        names: List<String>,
        prices: List<String>,
        descriptions: List<String>,
        images: List<String>,
        quantities: List<Int>,
        ingredients: List<String>
    ) {
        foodNames.clear()
        foodPrices.clear()
        foodDescriptions.clear()
        foodImages.clear()
        foodQuantities.clear()
        foodIngredients.clear()

        foodNames.addAll(names)
        foodPrices.addAll(prices)
        foodDescriptions.addAll(descriptions)
        foodImages.addAll(images)
        foodQuantities.addAll(quantities)
        foodIngredients.addAll(ingredients)

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
                val quantity = foodQuantities[position]
                cartFoodName.text = foodNames[position]
                cartItemPrice.text = foodPrices[position]

                Glide.with(context).load(Uri.parse(foodImages[position])).into(cartFoodImage)

                cartItemQuantity.text = quantity.toString()

                minusButton.setOnClickListener {
                    if (quantity > 1) {
                        val newQuantity = quantity - 1
                        onUpdateQuantity(foodNames[position], newQuantity)
                    }
                }

                plusButton.setOnClickListener {
                    if (quantity < 10) {
                        val newQuantity = quantity + 1
                        onUpdateQuantity(foodNames[position], newQuantity)
                    }
                }

                deleteButton.setOnClickListener {
                    onRemoveItem(foodNames[position])
                }
            }
        }
    }
}