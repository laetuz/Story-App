package com.neotica.storyapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neotica.storyapp.databinding.ItemStoryBinding
import com.neotica.storyapp.retrofit.response.story.Story
import com.neotica.storyapp.util.formatDateTime

class MainAdapter(
    private val listStory: List<Story>,
    val context: Context,
    private val listener: StoryListener
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface StoryListener {
        fun onClick(story: Story)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = listStory[position]
        holder.binding.apply {
            tvItemName.text = story.name
            tvDate.text = formatDateTime(story.createdAt)

            Glide.with(context)
                .load(story.photoUrl)
                .into(ivItemPhoto)
        }
        holder.itemView.setOnClickListener {
            listener.onClick(story)
        }
    }
}