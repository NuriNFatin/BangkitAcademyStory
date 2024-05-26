package com.example.bangkitacademystory.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bangkitacademystory.R
import com.example.bangkitacademystory.Remote.response.LoginResponse
import com.example.bangkitacademystory.ui.ViewModel.SignInViewModel
import com.example.bangkitacademystory.ui.ViewModel.ViewModelFactory
import com.example.bangkitacademystory.databinding.ActivitySignInBinding
import com.example.bangkitacademystory.Source.Result

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val signInViewModel by viewModels<SignInViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.asignin) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (validateInput(email, password)) {
                signInViewModel.signInUser(email, password)
            } else {
                Toast.makeText(
                    this@SignInActivity,
                    "Please fill the form correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        observerLoginResult()
    }

    private fun observerLoginResult() {
        signInViewModel.signInResult.observe(this@SignInActivity) { response ->
            Log.d("SignInActivity", "observerLoginResult called with response: $response")
            when (response) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Error -> {
                    showLoading(false)
                    Log.d("SignInActivity", "SignIn Error: ${response.error}")
                    Toast.makeText(
                        this@SignInActivity,
                        response.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Success -> {
                    showLoading(false)
                    Log.d("SignInActivity", "SignIn Success: ${response.data}")
                    handleSuccess(response.data)
                }
                else -> {
                    showLoading(false)
                    Log.d("SignInActivity", "Unknown state")
                }
            }
        }
    }

    private fun handleSuccess(data: LoginResponse) {
        AlertDialog.Builder(this@SignInActivity).apply {
            setTitle("Login Success")
            setMessage(data.message)
            setPositiveButton("Next") { _, _ ->
                navigateToMain()
            }
            create()
            show()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInput(email: String, password: String): Boolean {
        val isValid = email.isNotEmpty() && password.length >= 8

        if (!isValid) {
            val errorMessage = StringBuilder()

            if (password.length < 8) {
                errorMessage.append("Password must be at least 8 characters long.\n")
            }

            Toast.makeText(
                this@SignInActivity,
                errorMessage.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }

        return isValid
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun finish() {
        super.finish()
    }
}