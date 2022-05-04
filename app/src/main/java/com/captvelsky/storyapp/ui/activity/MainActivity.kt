package com.captvelsky.storyapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.captvelsky.storyapp.ui.activity.HomeActivity.Companion.EXTRA_TOKEN
import com.captvelsky.storyapp.ui.model.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewModelScope.launch {
            viewModel.getAuthToken().collect { token ->
                if (token.isNullOrEmpty()) {
                    Intent(this@MainActivity, AuthActivity::class.java).also { intent ->
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Intent(this@MainActivity, HomeActivity::class.java).also { intent ->
                        intent.putExtra(EXTRA_TOKEN, token)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}