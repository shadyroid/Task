package com.xontel.task.classes.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.softxpert.petfinder.databinding.ItemImageBinding
import com.xontel.domain.entity.beans.ImageBean
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ImagesAdapter @Inject constructor() :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    private val data: MutableList<ImageBean> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewImage: Int): ViewHolder = ViewHolder(
        ItemImageBinding.inflate(
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
            .into(holder.binding.ivImage)

    }


    override fun getItemCount(): Int {
        return data.size
    }

    fun addData(data: List<ImageBean>?) {
        val lastIndex = if (this.data.isEmpty()) 0 else this.data.size - 1
        this.data.addAll(data!!)
        val newLastIndex = this.data.size
        notifyItemRangeChanged(lastIndex, newLastIndex)
    }


    inner class ViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}