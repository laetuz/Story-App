package com.neotica.storyapp.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neotica.storyapp.databinding.ItemStoryBinding
import com.neotica.storyapp.ui.DetailStoryFragmentArgs
import com.neotica.storyapp.retrofit.response.story.Story
import com.neotica.storyapp.util.formatDateTime

class MainPagingAdapter(
    private val context: Context,
    private val listener: MainAdapter.StoryListener
) : PagingDataAdapter<Story, MainPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        holder.binding.apply {
            tvItemName.text = story?.name
            if (story != null) {
                tvDate.text = formatDateTime(story.createdAt)
            }

            Glide.with(context)
                .load(story?.photoUrl)
                .into(ivItemPhoto)
        }
        holder.itemView.setOnClickListener {
            story?.let { listener.onClick(it) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}