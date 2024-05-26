package com.example.bangkitacademystory.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bangkitacademystory.Remote.response.RegisterResponse
import com.example.bangkitacademystory.Repository.BaRepository
import com.example.bangkitacademystory.Source.Result
import kotlinx.coroutines.launch

class SignUpViewModel(private val baRepository: BaRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<Result<RegisterResponse>>()
    val responseResultState: LiveData<Result<RegisterResponse>> get() = _responseResult

    fun signUpUser(name: String, email: String, password: String) {
        _responseResult.value = Result.Loading

        viewModelScope.launch {
            val result = baRepository.register(name, email, password)
            _responseResult.value = result
        }
    }
}