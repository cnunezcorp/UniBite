package com.unibite.app.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.unibite.app.R
import com.unibite.app.data.CartRepository
import com.unibite.app.databinding.FragmentCartBinding
import com.unibite.app.domain.usecase.CartUseCase
import com.unibite.app.model.CartItemsModel
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
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeCartItems()
        setupProceedButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchCartItems()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            requireContext(),
            viewModel::removeCartItem,
            viewModel::updateCartItemQuantity
        )
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun observeCartItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.cartItems.collect { items ->
                        cartAdapter.updateItems(
                            items.map { it.foodName ?: "" },
                            items.map { it.foodPrice ?: "" },
                            items.map { it.foodDescription ?: "" },
                            items.map { it.foodImage ?: "" },
                            items.map { it.foodQuantity },
                            items.map { it.foodIngredient ?: "" }
                        )
                    }
                }

                launch {
                    viewModel.loading.collect { isLoading ->
                        // Implementa la lógica de carga si lo deseas
                    }
                }

                launch {
                    viewModel.error.collect { error ->
                        error?.let {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupProceedButton() {
        binding.proceedButton.setOnClickListener {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }
    }
}