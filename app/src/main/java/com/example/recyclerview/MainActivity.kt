package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerview.databinding.ActivityMainBinding
import com.example.recyclerview.databinding.SongCardViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cardViewBinding: SongCardViewBinding
    private lateinit var adapter: SongAdapter
    private val songList = ArrayList<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        cardViewBinding = SongCardViewBinding.inflate(layoutInflater)

        lifecycleScope.launch(Dispatchers.Default) {
            for(i in Constants.songName.indices) {
                songList.add(Song(Constants.PosterList[i], Constants.songName[i], Constants.ArtistName[i]))
            }
        }
        adapter = SongAdapter(songList, this, true)
        binding.rvSongCollection.adapter = adapter

        // Adding card view over recycler view
        binding.cardViewContainer.addView(cardViewBinding.root)

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                adapter.removeItem(position)
            }

        }).attachToRecyclerView(binding.rvSongCollection)

        setContentView(binding.root)
    }

    override fun onItemClicked(adapterPosition: Int) {
        Toast.makeText(this, "Playing ${songList[adapterPosition].name} by ${songList[adapterPosition].artist}", Toast.LENGTH_LONG).show()

        // Binding the song details to card view
        cardViewBinding.apply {
            Glide.with(this.imageView)
                .load(songList[adapterPosition].poster)
                .into(this.imageView)
            tvSongName.text = songList[adapterPosition].name
            tvArtistName.text = songList[adapterPosition].artist
            cardViewBinding.root.visibility = View.VISIBLE
        }

        // Background recycler view items are disable for click
        adapter.setClickable(true)
    }
}