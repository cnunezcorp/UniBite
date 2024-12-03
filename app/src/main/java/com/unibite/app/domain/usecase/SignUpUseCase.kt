package com.unibite.app.domain.usecase

import com.unibite.app.domain.repository.AuthRepository
import com.unibite.app.model.UserModel

class SignUpUseCase(private val repository: AuthRepository) {
    fun createAccount(userModel: UserModel, password: String, onResult: (Boolean, String?) -> Unit) {
        repository.createAccount(userModel, password, onResult)
    }

    fun signInWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        repository.signInWithGoogle(idToken, onResult)
    }
}