package com.captvelsky.storyapp.data.local

import android.util.Log
import com.captvelsky.storyapp.data.remote.response.StoryUploadResponse
import com.captvelsky.storyapp.data.remote.response.LoginResponse
import com.captvelsky.storyapp.data.remote.response.RegisterResponse
import com.captvelsky.storyapp.data.remote.response.StoryResponse
import com.captvelsky.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val apiService: ApiService, private val preferences: UserPreferences
) {

    fun getAuthToken(): Flow<String?> = preferences.getUserToken()

    suspend fun setAuthToken(token: String) {
        preferences.setUserToken(token)
    }

    suspend fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> = flow {
        try {
            val registerResponse = apiService.userRegister(name, email, password)
            Log.d("200", "Register Success")
            emit(Result.success(registerResponse))
        } catch (e: Exception) {
            Log.e("400", "Register Failed")
            emit(Result.failure(e))
        }
    }

    suspend fun userLogin(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.userLogin(email, password)
            Log.d("200", "Login Success")
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("400", "Login Failed")
            emit(Result.failure(e))
        }
    }

    suspend fun getStories(
        token: String,
        page: Int? = null,
        size: Int? = null
    ): Flow<Result<StoryResponse>> = flow {
        try {
            val userToken = "Bearer $token"
            val response = apiService.getStories(userToken, page, size)
            Log.d("200", "Success")
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("400", "Failed")
            emit(Result.failure(e))
        }
    }

    fun getStoriesLocation(token: String): Flow<Result<StoryResponse>> = flow {
        try {
            val userToken = "Bearer $token"
            val response = apiService.getStories(userToken, size = 30, location = 1)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Flow<Result<StoryUploadResponse>> = flow {
        try {
            val userToken = "Bearer $token"
            val response = apiService.uploadImage(userToken, file, description)
            Log.d("200", "Success")
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("400", "Failed")
            emit(Result.failure(e))
        }
    }
}