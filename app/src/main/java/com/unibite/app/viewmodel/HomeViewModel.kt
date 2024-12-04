package com.unibite.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibite.app.domain.usecase.GetPopularItemsUseCase
import com.unibite.app.model.MenuItemModel
import kotlinx.coroutines.launch

class HomeViewModel(private val getPopularItemsUseCase: GetPopularItemsUseCase) : ViewModel() {

    private val _popularItems = MutableLiveData<List<MenuItemModel>>()
    val popularItems: LiveData<List<MenuItemModel>> get() = _popularItems

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Obtiene los elementos populares y los expone a la UI
    fun fetchPopularItems() {
        viewModelScope.launch {
            try {
                val items = getPopularItemsUseCase.execute()
                _popularItems.value = items
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }
}