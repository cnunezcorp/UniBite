package com.unibite.app.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.unibite.app.model.MenuItemModel
import kotlinx.coroutines.tasks.await

class MenuRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val foodRef: DatabaseReference = database.reference.child("menu")

    suspend fun getMenuItems(): List<MenuItemModel> {
        return try {
            val snapshot = foodRef.get().await()
            snapshot.children.mapNotNull { it.getValue(MenuItemModel::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}