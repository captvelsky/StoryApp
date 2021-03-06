package com.captvelsky.storyapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.captvelsky.storyapp.R
import com.captvelsky.storyapp.adapter.LoadingStateAdapter
import com.captvelsky.storyapp.adapter.StoryAdapter
import com.captvelsky.storyapp.databinding.ActivityHomeBinding
import com.captvelsky.storyapp.ui.model.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        showLoading(true)

        storyAdapter = StoryAdapter()
        recyclerView = binding.rvStories
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
        recyclerView.setHasFixedSize(true)

        viewModel.getStories(intent.getStringExtra(EXTRA_TOKEN)!!).observe(this) {
            storyAdapter.submitData(lifecycle, it)
            showLoading(false)
        }

        binding.btnCreateStory.setOnClickListener {
            Intent(this, UploadActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.locationMenu -> {
                Intent(this, MapsActivity::class.java).also {
                    startActivity(it)
                }
                return true
            }
            R.id.languageMenu -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            R.id.logoutMenu -> {
                viewModel.deleteAuthToken()
                Intent(this, AuthActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }

}