package com.example.bangkitacademystory.ui.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bangkitacademystory.R
import com.example.bangkitacademystory.ui.ViewModel.GeneralViewModel
import com.example.bangkitacademystory.ui.ViewModel.ViewModelFactory
import com.example.bangkitacademystory.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val mainViewModel by viewModels<GeneralViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.alogin )) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
        checkSession()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        binding.apply {
            signinButton.setOnClickListener {
                Toast.makeText(this@LoginActivity, "Sign In button clicked", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, SignInActivity::class.java))
            }
            signupButton.setOnClickListener {
                Toast.makeText(this@LoginActivity, "Sign Up button clicked", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun checkSession() {
        mainViewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                navigateToHome()
            }
        }
    }
}
