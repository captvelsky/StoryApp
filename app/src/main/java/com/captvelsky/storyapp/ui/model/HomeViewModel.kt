package com.captvelsky.storyapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.captvelsky.storyapp.data.local.AppRepository
import com.captvelsky.storyapp.data.remote.response.StoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private var repository: AppRepository) : ViewModel() {

    fun deleteAuthToken() {
        viewModelScope.launch {
            repository.setAuthToken("")
        }
    }

    suspend fun getStories(token: String): Flow<Result<StoryResponse>> {
        return repository.getStories(token, null, null)
    }
}