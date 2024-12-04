package com.unibite.app.domain.usecase

import com.unibite.app.data.MenuRepository
import com.unibite.app.model.MenuItemModel

class MenuUseCase(private val repository: MenuRepository) {
    suspend fun fetchMenuItems(): List<MenuItemModel> = repository.getMenuItems()
}