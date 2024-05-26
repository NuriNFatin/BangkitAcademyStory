package com.example.bangkitacademystory.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.bangkitacademystory.R
import com.example.bangkitacademystory.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(NAME_KEY)
        val description = intent.getStringExtra(DESCRIPTION_KEY)
        val picture = intent.getStringExtra(PICTURE_KEY)

        binding.username.text = name
        binding.detailDescription.text = description
        Glide.with(this).load(picture).into(binding.ivDetailPhoto)

        binding.ivDetailPhoto.transitionName = "photo"
        binding.username.transitionName = "name"
        binding.detailDescription.transitionName = "description"

        ViewCompat.setOnApplyWindowInsetsListener(binding.detail) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        const val NAME_KEY = "name"
        const val DESCRIPTION_KEY = "description"
        const val PICTURE_KEY = "picture"
    }

    override fun finish() {
        super.finish()
    }
}
