package com.example.bangkitacademystory.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bangkitacademystory.Repository.BaRepository
import com.example.bangkitacademystory.Source.User
import kotlinx.coroutines.launch

class GeneralViewModel(private val repository: BaRepository) : ViewModel() {
    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    init {
        getStories()
    }

    fun getStories() = repository.getStories()
}