package com.unibite.app.viewmodel.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unibite.app.domain.usecase.LoginUseCase

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    val loginResult = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun login(email: String, password: String) {
        loginUseCase.login(email, password) { success, error ->
            if (success) {
                loginResult.postValue(true)
            } else {
                errorMessage.postValue(error ?: "Error desconocido")
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        loginUseCase.signInWithGoogle(idToken) { success, error ->
            if (success) {
                loginResult.postValue(true)
            } else {
                errorMessage.postValue(error ?: "Error desconocido")
            }
        }
    }
}