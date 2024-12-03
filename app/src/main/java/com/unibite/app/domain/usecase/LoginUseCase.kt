package com.unibite.app.domain.usecase

import com.unibite.app.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        repository.login(email, password, onResult)
    }

    fun signInWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        repository.signInWithGoogle(idToken, onResult)
    }
}