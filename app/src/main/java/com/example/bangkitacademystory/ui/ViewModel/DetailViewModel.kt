package com.example.bangkitacademystory.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bangkitacademystory.Remote.response.DetailStoryResponse
import com.example.bangkitacademystory.Repository.BaRepository
import com.example.bangkitacademystory.Source.Result
import kotlinx.coroutines.launch


class DetailViewModel (private val baRepository: BaRepository) : ViewModel() {
    private val _detailStory = MutableLiveData<Result<DetailStoryResponse>>()
    val detailStory: LiveData<Result<DetailStoryResponse>> get() = _detailStory

    fun fetchStoryDetail(storyId: String) {
        _detailStory.value = Result.Loading

        viewModelScope.launch {
            val result = baRepository.getStoryDetail(storyId)
            _detailStory.value = result
        }
    }
}