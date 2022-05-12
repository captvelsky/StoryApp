package com.captvelsky.storyapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.captvelsky.storyapp.data.local.AppRepository
import com.captvelsky.storyapp.data.remote.response.StoryUploadResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class UploadViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    fun getAuthToken(): Flow<String?> = repository.getAuthToken()

    suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Result<StoryUploadResponse>> =
        repository.uploadImage(token, file, description, lat, lon)
}