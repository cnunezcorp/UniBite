package com.unibite.app.domain.repository

import com.unibite.app.model.UserModel

interface AuthRepository {
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit)
    fun signInWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit)
    fun createAccount(userModel: UserModel, password: String, onResult: (Boolean, String?) -> Unit)
}