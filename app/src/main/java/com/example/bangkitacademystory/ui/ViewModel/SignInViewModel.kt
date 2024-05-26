package com.example.bangkitacademystory.ui.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bangkitacademystory.Remote.response.LoginResponse
import com.example.bangkitacademystory.Repository.BaRepository
import kotlinx.coroutines.launch
import com.example.bangkitacademystory.Source.Result


class SignInViewModel (private val baRepository: BaRepository) : ViewModel() {
    private var _signInResult = MutableLiveData<Result<LoginResponse>>()
    val signInResult: LiveData<Result<LoginResponse>> get() = _signInResult

    fun signInUser(email: String, password: String) {
        _signInResult.value = Result.Loading

        viewModelScope.launch {
            try {
                val result = baRepository.loginUser(email, password)
                _signInResult.value = result
                Log.d("SignInViewModel", "Result received: $result")
            } catch (e: Exception) {
                _signInResult.value = Result.Error(e.message ?: "An error occurred")
                Log.d("SignInViewModel", "Exception: ${e.message}")
            }
        }
    }
}