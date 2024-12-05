package com.unibite.app.viewmodel.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibite.app.domain.usecase.MenuUseCase
import com.unibite.app.model.MenuItemModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MenuViewModel(private val useCase: MenuUseCase) : ViewModel() {

    private val _menuItems = MutableStateFlow<List<MenuItemModel>>(emptyList())
    val menuItems: StateFlow<List<MenuItemModel>> get() = _menuItems

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    fun fetchMenu() {
        viewModelScope.launch {
            _loading.value = true
            _menuItems.value = useCase.fetchMenuItems()
            _loading.value = false
        }
    }
}