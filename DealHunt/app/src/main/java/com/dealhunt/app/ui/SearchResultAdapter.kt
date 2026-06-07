package com.dealhunt.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dealhunt.app.R
import com.dealhunt.app.databinding.ItemSearchResultBinding
import com.dealhunt.app.model.GameSearchResult

class SearchResultAdapter(
    private val onItemClick: (GameSearchResult) -> Unit
) : ListAdapter<GameSearchResult, SearchResultAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: GameSearchResult) {
            binding.tvGameTitle.text = game.title

            val price = game.cheapest.toDoubleOrNull() ?: 0.0
            binding.tvPrice.text = if (price == 0.0) "ÜCRETSİZ" else "₺${String.format("%.2f", price)}"

            Glide.with(binding.root.context)
                .load(game.thumbnail)
                .placeholder(R.drawable.placeholder_game)
                .centerCrop()
                .into(binding.ivThumbnail)

            binding.root.setOnClickListener { onItemClick(game) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<GameSearchResult>() {
        override fun areItemsTheSame(a: GameSearchResult, b: GameSearchResult) = a.gameId == b.gameId
        override fun areContentsTheSame(a: GameSearchResult, b: GameSearchResult) = a == b
    }
}
