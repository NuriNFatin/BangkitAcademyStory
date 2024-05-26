package com.example.bangkitacademystory.ui.view

import android.content.Intent
import android.os.Bundle
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bangkitacademystory.R
import com.example.bangkitacademystory.Source.Result
import com.example.bangkitacademystory.Source.getImageUri
import com.example.bangkitacademystory.Source.reduceFileImage
import com.example.bangkitacademystory.Source.uriToFile
import com.example.bangkitacademystory.ui.ViewModel.AddViewModel
import com.example.bangkitacademystory.ui.ViewModel.ViewModelFactory
import com.example.bangkitacademystory.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private val addStoryViewModel by viewModels<AddViewModel> {ViewModelFactory.getInstance(this)    }
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViews()
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.tvAddImg.setImageURI(it)
        }
    }

    private fun setupViews() {
        binding.apply {
            btnAddCamera.setOnClickListener {
                startCamera()
            }
            btnAddGallery.setOnClickListener {
                startGallery()
            }
            buttonAdd.setOnClickListener {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        currentImageUri?.let {
            val image = uriToFile(it, this).reduceFileImage()
            val description = binding.edAddDescription.text.toString()

            addStoryViewModel.uploadStories(image, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, R.string.failed, Toast.LENGTH_SHORT).show()
                        }

                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun finish() {
        super.finish()
    }
}
