package com.example.bangkitacademystory.ui.ViewModel

import androidx.lifecycle.ViewModel
import com.example.bangkitacademystory.Repository.BaRepository
import java.io.File

class AddViewModel  (private val baRepository: BaRepository) : ViewModel() {
    fun uploadStories(file: File, description: String) =
        baRepository.uploadStories(file, description)
}