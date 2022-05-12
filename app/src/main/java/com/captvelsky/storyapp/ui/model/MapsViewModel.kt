package com.captvelsky.storyapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.captvelsky.storyapp.data.local.AppRepository
import com.captvelsky.storyapp.data.remote.response.StoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    fun getStoriesLocation(token: String): Flow<Result<StoryResponse>> =
        repository.getStoriesLocation(token)

    fun getAuthToken(): Flow<String?> = repository.getAuthToken()
}