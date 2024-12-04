package com.unibite.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//facilita el paso de objetos entre actividades y fragmentos
@Parcelize
data class MenuItemModel(
    val foodName: String? = null,
    val foodPrice: String? = null,
    val foodDescription: String? = null,
    val foodImage: String? = null,
    val foodIngredient: String? = null,
) : Parcelable
