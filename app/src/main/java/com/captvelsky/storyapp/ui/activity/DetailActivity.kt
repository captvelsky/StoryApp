package com.captvelsky.storyapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.captvelsky.storyapp.data.local.StoryResponseItem
import com.captvelsky.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var story: StoryResponseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        showLoading(true)
        story = intent.getParcelableExtra(EXTRA_DETAIL)!!

        binding.apply {
            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .into(ivStory)
            tvUsername.text = story.name
            tvDesc.text = story.description
            showLoading(false)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}