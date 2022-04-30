package com.captvelsky.storyapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.captvelsky.storyapp.data.local.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    suspend fun userLogin(email: String, password: String) = repository.userLogin(email, password)

    fun saveAuthToken(token: String) = viewModelScope.launch {
        repository.setAuthToken(token)
    }
}