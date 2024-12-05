package com.unibite.app.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unibite.app.domain.usecase.GetPopularItemsUseCase

class HomeViewModelFactory(private val getPopularItemsUseCase: GetPopularItemsUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(getPopularItemsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}