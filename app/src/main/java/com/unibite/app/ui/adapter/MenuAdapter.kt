package com.unibite.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unibite.app.databinding.MenuItemBinding
import com.unibite.app.ui.activity.DetailsActivity

class MenuAdapter (private val MenuItemsName :MutableList<String>, private val MenuItemPrice :MutableList<String>, private val MenuImage :MutableList<Int>, private val requireContext: Context) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val itemClickListener: OnClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = MenuItemsName.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) :RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener{
                val position = adapterPosition
                if (position !=RecyclerView.NO_POSITION){
                    itemClickListener?.onItemClick(position)
                }

                //Accion de Click para abrir los detalles de la comida seleccionada
                var intent = Intent(requireContext, DetailsActivity::class.java)
                intent.putExtra("MenuItemName", MenuItemsName.get(position))
                intent.putExtra("MenuItemImage", MenuImage.get(position))
                requireContext.startActivity(intent)
            }
        }
        fun bind(position: Int){
            binding.apply {
                menuFoodName.text = MenuItemsName[position]
                menuPrice.text = MenuItemPrice[position]
                menuImage.setImageResource(MenuImage[position])
            }
        }

    }
    interface OnClickListener{
        fun onItemClick(position: Int)
    }
}


