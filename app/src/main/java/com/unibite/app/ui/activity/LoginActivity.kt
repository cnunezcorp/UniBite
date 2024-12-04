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
import com.adminunibite.app.viewmodel.LoginViewModel
import com.adminunibite.app.viewmodel.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.unibite.app.R
import com.unibite.app.data.AuthRepositoryImpl
import com.unibite.app.databinding.ActivityLoginBinding
import com.unibite.app.domain.usecase.LoginUseCase

class LoginActivity : AppCompatActivity() {

    private val auth : FirebaseAuth = Firebase.auth
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val binding:ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val loginViewModel: LoginViewModel by lazy {
        val authRepository = AuthRepositoryImpl(Firebase.auth, databaseReference)
        val loginUseCase = LoginUseCase(authRepository)
        val factory = LoginViewModelFactory(loginUseCase)
        ViewModelProvider(this, factory)[LoginViewModel::class.java]
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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)!!
                Log.d("GoogleSignIn", "firebaseAuthWithGoogle:" + account.id)
                loginViewModel.firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("GoogleSignIn", "Fallo en Google Sign In", e)
            }
        }
    }

    /*override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }*/

    private fun setupUI() {
        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Debe Llenar Todos los Datos", Toast.LENGTH_LONG).show()
            }
            else{
                Log.d("Login", "Email: $email")
                Log.d("Login", "Password: $password")
                loginViewModel.login(email, password)
            }
        }
        observeViewModel()

        binding.dontHaveButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.googleLoginButton.setOnClickListener{
            val signIntent = googleSignInClient.signInIntent
            startActivityForResult(signIntent, RC_SIGN_IN)
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginResult.observe(this) { success ->
            if (success){
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                updateUI(auth.currentUser)
            } else {
                Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_LONG).show()
            }
        }

        loginViewModel.errorMessage.observe(this) {error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}