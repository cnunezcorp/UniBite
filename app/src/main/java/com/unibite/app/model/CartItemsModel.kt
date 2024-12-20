package com.unibite.app.model

data class CartItemsModel(
    val foodName: String? = null,
    val foodPrice: String? = null,
    val foodDescription: String? = null,
    val foodImage: String? = null,
    val foodQuantity: Int = 1,
    val foodIngredient: String? = null,
    var itemKey: String? = null // Clave única en Firebase
)
