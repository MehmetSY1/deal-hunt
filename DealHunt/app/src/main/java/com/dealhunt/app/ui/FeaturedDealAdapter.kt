package com.dealhunt.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dealhunt.app.R
import com.dealhunt.app.databinding.ItemFeaturedDealBinding
import com.dealhunt.app.model.DealDetail

class FeaturedDealAdapter(
    private val onItemClick: (DealDetail) -> Unit
) : ListAdapter<DealDetail, FeaturedDealAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemFeaturedDealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(deal: DealDetail) {
            binding.tvTitle.text = deal.title

            val sale = deal.salePrice.toDoubleOrNull() ?: 0.0
            val normal = deal.normalPrice.toDoubleOrNull() ?: 0.0
            val savings = deal.savings.toDoubleOrNull() ?: 0.0

            binding.tvSalePrice.text = if (sale == 0.0) "ÜCRETSİZ" else "₺${String.format("%.2f", sale)}"
            binding.tvNormalPrice.text = "₺${String.format("%.2f", normal)}"

            if (savings > 0) {
                binding.tvSavings.text = "-%${savings.toInt()}"
                binding.tvSavings.visibility = android.view.View.VISIBLE
            } else {
                binding.tvSavings.visibility = android.view.View.GONE
            }

            Glide.with(binding.root.context)
                .load(deal.thumb)
                .placeholder(R.drawable.placeholder_game)
                .centerCrop()
                .into(binding.ivThumb)

            binding.root.setOnClickListener { onItemClick(deal) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFeaturedDealBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<DealDetail>() {
        override fun areItemsTheSame(a: DealDetail, b: DealDetail) = a.dealId == b.dealId
        override fun areContentsTheSame(a: DealDetail, b: DealDetail) = a == b
    }
}
