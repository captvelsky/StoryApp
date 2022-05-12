package com.captvelsky.storyapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.util.Pair
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.captvelsky.storyapp.data.local.StoryResponseItem
import com.captvelsky.storyapp.databinding.ItemRowStoriesBinding
import com.captvelsky.storyapp.ui.activity.DetailActivity
import com.captvelsky.storyapp.ui.activity.DetailActivity.Companion.EXTRA_DETAIL

class StoryAdapter : PagingDataAdapter<StoryResponseItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: ItemRowStoriesBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(context: Context, story: StoryResponseItem) {
            binding.apply {
                Glide.with(context)
                    .load(story.photoUrl)
                    .into(ivStory)
                tvUsername.text = story.name
                tvDesc.text = story.description

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(ivStory, "image"),
                            Pair(tvUsername, "username"),
                            Pair(tvDesc, "description")
                        )

                    Intent(context, DetailActivity::class.java).also {
                        it.putExtra(EXTRA_DETAIL, story)
                        context.startActivity(it, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holder.itemView.context, getItem(position)!!)
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryResponseItem> =
            object : DiffUtil.ItemCallback<StoryResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: StoryResponseItem,
                    newItem: StoryResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: StoryResponseItem,
                    newItem: StoryResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}