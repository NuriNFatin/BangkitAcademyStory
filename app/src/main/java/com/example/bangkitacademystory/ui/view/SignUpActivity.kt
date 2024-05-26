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
import com.example.bangkitacademystory.Remote.response.RegisterResponse
import com.example.bangkitacademystory.ui.ViewModel.SignUpViewModel
import com.example.bangkitacademystory.ui.ViewModel.ViewModelFactory
import com.example.bangkitacademystory.databinding.ActivitySignUpBinding
import com.example.bangkitacademystory.Source.Result

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel by viewModels<SignUpViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.asignup) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        observeSignUpResult()

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            Log.d("SignUpActivity", "Sign up button clicked with name: $name, email: $email, password: $password")

            if (validateInput(name, email, password)) {
                Log.d("SignUpActivity", "Input validation passed")
                signUpViewModel.signUpUser(name, email, password)
            } else {
                Log.d("SignUpActivity", "Input validation failed")
                Toast.makeText(
                    this@SignUpActivity,
                    "Please fill the form correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeSignUpResult() {
        signUpViewModel.responseResultState.observe(this@SignUpActivity) { response ->
            when (response) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    Log.e("SignUpActivity", "Error: ${response.error}")
                    Toast.makeText(
                        this@SignUpActivity,
                        response.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Result.Success -> {
                    showLoading(false)
                    handleSuccess(response.data)
                }
                else -> showLoading(false)
            }
        }
    }

    private fun handleSuccess(data: RegisterResponse) {
        AlertDialog.Builder(this@SignUpActivity).apply {
            setTitle("Sign Up Success, Welcome to Bangkit Stories")
            setMessage(data.message)
            setPositiveButton("Go Sign In") { _, _ ->
                navigateToSignIn()
            }
            create()
            show()
        }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        val isNameValid = name.isNotEmpty()
        val isEmailValid = email.isNotEmpty()
        val isPasswordValid = password.length >= 8

        Log.d("Validation", "Name valid: $isNameValid, Email valid: $isEmailValid, Password valid: $isPasswordValid")

        if (!isNameValid || !isEmailValid || !isPasswordValid) {
            val errorMessage = StringBuilder()

            if (!isNameValid) {
                errorMessage.append("Name must not be empty.\n")
            }

            if (!isEmailValid) {
                errorMessage.append("Email must not be empty.\n")
            }

            if (!isPasswordValid) {
                errorMessage.append("Password must be at least 8 characters long.\n")
            }

            Toast.makeText(
                this@SignUpActivity,
                errorMessage.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }

        return isNameValid && isEmailValid && isPasswordValid
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun finish() {
        super.finish()
    }
}