package com.xontel.task.classes.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.softxpert.petfinder.databinding.ItemVideoBinding
import com.xontel.domain.entity.beans.VideoBean
import javax.inject.Inject

class VideosAdapter @Inject constructor() :
    RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
    private val data: MutableList<VideoBean> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewVideo: Int): ViewHolder = ViewHolder(
        ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.getContext())
            .load(Uri.parse(data[position].address))
            .apply(RequestOptions().override(150, 150))
            .centerCrop()
            .into(holder.binding.ivVideo)

    }


    override fun getItemCount(): Int {
        return data.size
    }

    fun addData(data: List<VideoBean>?) {
        val lastIndex = if (this.data.isEmpty()) 0 else this.data.size - 1
        this.data.addAll(data!!)
        val newLastIndex = this.data.size
        notifyItemRangeChanged(lastIndex, newLastIndex)
    }


    inner class ViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}