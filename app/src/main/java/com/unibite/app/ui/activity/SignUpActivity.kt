package com.unibite.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.unibite.app.viewmodel.signUp.SignUpViewModel
import com.unibite.app.viewmodel.signUp.SignUpViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.unibite.app.R
import com.unibite.app.data.AuthRepositoryImpl
import com.unibite.app.databinding.ActivitySignBinding
import com.unibite.app.domain.usecase.SignUpUseCase
import com.unibite.app.model.UserModel

class SignUpActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = Firebase.auth

    private val signUpViewModel: SignUpViewModel by lazy {
        val authRepository = AuthRepositoryImpl(auth, Firebase.database.reference)
        val signUpUseCase = SignUpUseCase(authRepository)
        val factory = SignUpViewModelFactory(signUpUseCase)
        ViewModelProvider(this, factory)[SignUpViewModel::class.java]
    }

    private val binding: ActivitySignBinding by lazy{
        ActivitySignBinding.inflate(layoutInflater)
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(this, gso)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()

        binding.alreadyHaveButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("GoogleSignIn", "firebaseAuthWithGoogle:" + account.id)
                signUpViewModel.signInWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("GoogleSignIn", "Google Sign-In failed", e)
            }
        }
    }

    private fun setupUI() {
        binding.registerButton.setOnClickListener {
            val name = binding.signUpName.text.toString().trim()
            val email = binding.signUpEmail.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Debe llenar todos los datos", Toast.LENGTH_LONG).show()
            } else {
                val userModel = UserModel(
                    name = name,
                    email = email
                )
                signUpViewModel.createAccount(userModel, password)
            }
        }

        observeViewModel()

        binding.googleRegisterButton.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            startActivityForResult(signIntent, RC_SIGN_IN)
        }
    }

    private fun observeViewModel() {
        signUpViewModel.signUpResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error al crear la cuenta", Toast.LENGTH_LONG).show()
            }
        }

        signUpViewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

}
