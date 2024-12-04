package com.unibite.app.domain.usecase

import com.unibite.app.data.MenuRepository
import com.unibite.app.model.MenuItemModel

class GetPopularItemsUseCase(private val repository: MenuRepository) {
    // Obtiene los elementos populares de la lista de menú
    suspend fun execute(): List<MenuItemModel> {
        val items = repository.getMenuItems()
        return items.shuffled().take(6) // Toma 6 elementos aleatorios
    }
}