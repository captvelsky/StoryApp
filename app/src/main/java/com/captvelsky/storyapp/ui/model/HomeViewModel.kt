package com.captvelsky.storyapp.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.captvelsky.storyapp.data.local.AppRepository
import com.captvelsky.storyapp.data.local.database.StoryResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class HomeViewModel @Inject constructor(private var repository: AppRepository) : ViewModel() {

    fun deleteAuthToken() {
        viewModelScope.launch {
            repository.setAuthToken("")
        }
    }

    fun getStories(token: String): LiveData<PagingData<StoryResponseItem>> {
        return repository.getStories(token).cachedIn(viewModelScope).asLiveData()
    }
}