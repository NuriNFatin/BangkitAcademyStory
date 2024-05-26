package com.example.bangkitacademystory.ui.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bangkitacademystory.R
import com.example.bangkitacademystory.Remote.response.ListStoryItem
import com.example.bangkitacademystory.Source.Result
import com.example.bangkitacademystory.ui.ViewModel.ViewModelFactory
import com.example.bangkitacademystory.ui.MainAdapter
import com.example.bangkitacademystory.ui.ViewModel.GeneralViewModel
import com.example.bangkitacademystory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val generalViewModel by viewModels<GeneralViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val adapter by lazy { MainAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViews()
        observeStories()
    }

    private fun setupViews() {
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = this@MainActivity.adapter
        }

        binding.buttonAddStory.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    generalViewModel.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                R.id.action_language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                else -> false
            }
        }
    }

    private fun handleStories(stories: Result<List<ListStoryItem>>) {
        when (stories) {
            is Result.Success -> showStories(stories.data)
            is Result.Error -> showError(stories.error)
            is Result.Loading -> showLoading(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showStories(storyList: List<ListStoryItem>) {
        showLoading(false)
        adapter.setStories(storyList)
    }

    private fun showError(errorMessage: String) {
        showLoading(false)
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun observeStories() {
        generalViewModel.getStories().observe(this@MainActivity) { stories ->
            handleStories(stories)
        }
    }

    override fun finish() {
        super.finish()
    }
}
