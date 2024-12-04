package com.unibite.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unibite.app.domain.usecase.MenuUseCase

class MenuViewModelFactory(private val useCase: MenuUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MenuViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}