package com.unibite.app.viewmodel.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unibite.app.domain.usecase.SignUpUseCase
import com.unibite.app.model.UserModel

class SignUpViewModel(private val signUpUseCase: SignUpUseCase) : ViewModel() {
    val signUpResult = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun createAccount(userModel: UserModel, password: String) {
        signUpUseCase.createAccount(userModel, password) { success, error ->
            if (success) {
                signUpResult.postValue(true)
            } else {
                errorMessage.postValue(error ?: "Error desconocido")
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        signUpUseCase.signInWithGoogle(idToken) { success, error ->
            if (success) {
                signUpResult.postValue(true)
            } else {
                errorMessage.postValue(error ?: "Error con Google Sign-In")
            }
        }
    }
}