package com.example.bangkitacademystory.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.bangkitacademystory.Preferences.SettingPreferences
import com.example.bangkitacademystory.Remote.api.ApiConfig
import com.example.bangkitacademystory.Remote.api.ApiService
import com.example.bangkitacademystory.Remote.response.*
import com.example.bangkitacademystory.Source.Result
import com.example.bangkitacademystory.Source.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class BaRepository private constructor(
    private val apiService: ApiService,
    private val pref: SettingPreferences
) {

    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val response = apiService.register(name, email, password)
            if (response.error == true) {
                Result.Error(response.message ?: "Unknown error")
            } else {
                Result.Success(response)
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage ?: "Unknown error")
        }
    }

    suspend fun loginUser(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(email, password)
            val loginResult = response.loginResult
            if (loginResult?.name != null && loginResult.token != null) {
                val session = User(
                    name = loginResult.name,
                    email = email,
                    token = loginResult.token,
                    isLogin = true
                )
                saveSession(session)
                ApiConfig.getApiService(session.token)
                Result.Success(response)
            } else {
                Result.Error("Login result or its fields are null")
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message ?: "Unknown error"
            Result.Error(errorMessage)
        }
    }

    fun getStories(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories()
            val nonNullList = response.listStory?.mapNotNull { it } ?: emptyList()
            emit(Result.Success(nonNullList))
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val body = Gson().fromJson(error, ErrorResponse::class.java)
            emit(Result.Error(body?.message ?: "Error"))
        }
    }

    fun uploadStories(imageFile: File, description: String): LiveData<Result<NewStoryResponse>> =
        liveData {
            emit(Result.Loading)
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            try {
                val successResponse =
                    apiService.postNewStory(multipartBody, requestBody)
                if (successResponse.error == true) {
                    emit(Result.Error(successResponse.message ?: "Unknown error"))
                } else {
                    emit(Result.Success(successResponse))
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message ?: "Unknown error"
                emit(Result.Error(errorMessage))
            }
        }

    suspend fun getStoryDetail(storyId: String): Result<DetailStoryResponse> {
        return try {
            val response = apiService.getStoryDetail(storyId)
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message ?: "Unknown error"
            Result.Error(errorMessage)
        }
    }

    private suspend fun saveSession(user: User) {
        pref.saveSession(user)
    }

    fun getSession(): Flow<User> {
        return pref.getSession()
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: SettingPreferences
        ): BaRepository = BaRepository(apiService, userPreference)


    }
}
