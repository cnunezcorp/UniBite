package com.unibite.app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.unibite.app.domain.repository.AuthRepository
import com.unibite.app.model.UserModel
import javax.inject.Inject

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference
) : AuthRepository {
    override fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, null)
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }

    override fun signInWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val userModel = UserModel(it.email, null)
                    database.child("user").child(it.uid).setValue(userModel)
                }
                onResult(true, null)
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }

    override fun createAccount(userModel: UserModel, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(userModel.email!!, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser!!.uid
                database.child("user").child(userId).setValue(userModel)
                onResult(true, null)
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }
}