package com.unibite.app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.unibite.app.databinding.FragmentCartBinding
import com.unibite.app.ui.activity.PayOutActivity
import com.unibite.app.ui.adapter.CartAdapter
import com.unibite.app.viewmodel.CartViewModel
import com.unibite.app.viewmodel.CartViewModelFactory
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val viewModel: CartViewModel by viewModels { CartViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeCartItems()
        setupProceedButton()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchCartItems() // Obtiene los datos del carrito.
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            requireContext(),
            onRemoveItem = { foodName -> viewModel.removeCartItem(foodName) },
            onUpdateQuantity = { foodName, newQuantity -> viewModel.updateCartItemQuantity(foodName, newQuantity) }
        )
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun observeCartItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartItems.collect { items ->
                    if (items.isEmpty()) {
                        binding.cartRecyclerView.visibility = View.GONE
                    } else {
                        binding.cartRecyclerView.visibility = View.VISIBLE
                        cartAdapter.updateItems(
                            items.map { it.foodName ?: "" },
                            items.map { it.foodPrice ?: "" },
                            items.map { it.foodDescription ?: "" },
                            items.map { it.foodImage ?: "" },
                            items.map { it.foodQuantity },
                            items.map { it.foodIngredient ?: "" },
                            items.map { it.itemKey?: "" }
                        )
                    }
                }
            }
        }
    }

    private fun setupProceedButton() {
        binding.proceedButton.setOnClickListener {
            val cartItems = viewModel.cartItems.value
            if (cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "El carrito está vacío.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(requireContext(), PayOutActivity::class.java).apply {
                putStringArrayListExtra(
                    "FoodItemName",
                    ArrayList(cartItems.map { it.foodName ?: "" })
                )
                putStringArrayListExtra(
                    "FoodItemPrice",
                    ArrayList(cartItems.map { it.foodPrice ?: "" })
                )
                putStringArrayListExtra(
                    "FoodItemImage",
                    ArrayList(cartItems.map { it.foodImage ?: "" })
                )
                putIntegerArrayListExtra(
                    "FoodItemQuantities",
                    ArrayList(cartItems.map { it.foodQuantity })
                )
            }
            startActivity(intent)
        }
    }
}


