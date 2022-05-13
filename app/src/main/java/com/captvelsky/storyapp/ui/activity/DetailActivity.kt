package com.captvelsky.storyapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
            loadImage(story.photoUrl)
            tvUsername.text = story.name
            tvDesc.text = story.description
            showLoading(false)
        }
    }

    private fun loadImage(url: String?) {
        Glide.with(this@DetailActivity)
            .load(url)
            .into(binding.ivStory)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}