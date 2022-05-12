package com.captvelsky.storyapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.captvelsky.storyapp.data.local.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    suspend fun userRegister(name: String, email: String, password: String) =
        repository.userRegister(name, email, password)
}