package com.example.bangkitacademystory.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bangkitacademystory.Remote.response.ListStoryItem
import com.example.bangkitacademystory.databinding.StoryBinding
import com.example.bangkitacademystory.ui.view.DetailActivity

class MainAdapter : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    private var storyList: List<ListStoryItem> = emptyList()

    inner class MyViewHolder(private val binding: StoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: ListStoryItem?) {
            with(binding) {
                tvItemName.text = user?.name
                tvDescription.text = user?.description
                Glide.with(root).load(user?.photoUrl).into(binding.ivItemPhoto)

                root.setOnClickListener {
                    val intentDetail = Intent(it.context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.NAME_KEY, user?.name)
                        putExtra(DetailActivity.DESCRIPTION_KEY, user?.description)
                        putExtra(DetailActivity.PICTURE_KEY, user?.photoUrl)
                    }

                    val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        it.context as Activity,
                        Pair(binding.ivItemPhoto, "photo"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvDescription, "description")
                    )

                    it.context.startActivity(intentDetail, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StoryBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(storyList[position])
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    fun setStories(stories: List<ListStoryItem>) {
        storyList = stories
        notifyDataSetChanged()
    }
}
