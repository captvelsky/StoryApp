package com.captvelsky.storyapp.ui.model

import androidx.lifecycle.ViewModel
import com.captvelsky.storyapp.data.local.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    fun getAuthToken(): Flow<String?> = repository.getAuthToken()
}