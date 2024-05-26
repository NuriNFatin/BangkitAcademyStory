package com.example.bangkitacademystory.Source

import android.content.Context
import com.example.bangkitacademystory.Preferences.SettingPreferences
import com.example.bangkitacademystory.Preferences.dataStore
import com.example.bangkitacademystory.Remote.api.ApiConfig
import com.example.bangkitacademystory.Repository.BaRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): BaRepository {
        val pref = SettingPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return BaRepository.getInstance(apiService, pref)
    }
}
