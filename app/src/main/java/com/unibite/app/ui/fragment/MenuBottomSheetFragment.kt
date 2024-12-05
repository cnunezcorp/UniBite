package com.unibite.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unibite.app.data.MenuRepository
import com.unibite.app.databinding.FragmentMenuBottomSheetBinding
import com.unibite.app.domain.usecase.MenuUseCase
import com.unibite.app.model.MenuItemModel
import com.unibite.app.ui.adapter.MenuAdapter
import com.unibite.app.viewmodel.menu.MenuViewModel
import com.unibite.app.viewmodel.menu.MenuViewModelFactory


class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBottomSheetBinding
    private val menuViewModel: MenuViewModel by lazy {
        val repository = MenuRepository()
        val useCase = MenuUseCase(repository)
        val factory = MenuViewModelFactory(useCase)
        ViewModelProvider(this, factory)[MenuViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)
        binding.buttonBack.setOnClickListener{
            dismiss()
        }
        observeMenuItems()
        menuViewModel.fetchMenu()
        return binding.root
    }

    private fun observeMenuItems() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            menuViewModel.menuItems.collect { items ->
                setAdapter(items)
            }
        }
    }

    private fun setAdapter(menuItems: List<MenuItemModel>) {
        val adapter = MenuAdapter(menuItems, requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }
}