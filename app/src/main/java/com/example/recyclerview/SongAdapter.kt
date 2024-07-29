package com.example.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerview.databinding.SongItemViewBinding

interface ItemClickListener {
    fun onItemClicked(position: Int)
}

class SongAdapter(
    private val songList: MutableList<Song>,
    private val itemClickListener: ItemClickListener,
    private var isClickable: Boolean
): RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(
        private val binding: SongItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(position: Int) {
            this.binding.apply {
                Glide.with(this.ivPoster)
                    .load(songList[position].poster)
                    .override(200,200)
                    .into(this.ivPoster);
                tvName.text = songList[position].name
                tvArtist.text = songList[position].artist
            }
        }

        override fun onClick(p0: View?) {
            if(isClickable) {
                itemClickListener.onItemClicked(adapterPosition)
            }
        }

    }

    fun setClickable(clickable: Boolean) {
        isClickable = clickable
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        songList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.SongViewHolder {
        val binding = SongItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongAdapter.SongViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return songList.size
    }
}