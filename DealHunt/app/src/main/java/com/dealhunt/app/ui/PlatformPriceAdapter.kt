package com.dealhunt.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dealhunt.app.databinding.ItemPlatformPriceBinding
import com.dealhunt.app.model.PlatformPrice

class PlatformPriceAdapter(
    private val onBuyClick: (PlatformPrice) -> Unit
) : ListAdapter<PlatformPrice, PlatformPriceAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemPlatformPriceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(price: PlatformPrice) {
            binding.tvStoreName.text = price.storeName

            binding.tvCurrentPrice.text = if (price.currentPrice == 0.0) "ÜCRETSİZ"
            else "₺${String.format("%.2f", price.currentPrice)}"

            if (price.originalPrice > price.currentPrice) {
                binding.tvOriginalPrice.text = "₺${String.format("%.2f", price.originalPrice)}"
                binding.tvOriginalPrice.visibility = View.VISIBLE
                binding.tvDiscount.text = "-%${price.savingsPercent.toInt()}"
                binding.tvDiscount.visibility = View.VISIBLE
            } else {
                binding.tvOriginalPrice.visibility = View.GONE
                binding.tvDiscount.visibility = View.GONE
            }

            if (price.isBestDeal) {
                binding.badgeBest.visibility = View.VISIBLE
                binding.root.setBackgroundResource(com.dealhunt.app.R.drawable.bg_best_deal_card)
                binding.tvCurrentPrice.setTextColor(
                    binding.root.context.getColor(com.dealhunt.app.R.color.accent_green)
                )
            } else {
                binding.badgeBest.visibility = View.GONE
                binding.root.setBackgroundResource(com.dealhunt.app.R.drawable.bg_platform_card)
                binding.tvCurrentPrice.setTextColor(
                    binding.root.context.getColor(com.dealhunt.app.R.color.text_primary)
                )
            }

            if (price.logoUrl.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(price.logoUrl)
                    .into(binding.ivStoreLogo)
            }

            binding.btnBuy.setOnClickListener { onBuyClick(price) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlatformPriceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<PlatformPrice>() {
        override fun areItemsTheSame(a: PlatformPrice, b: PlatformPrice) = a.dealId == b.dealId
        override fun areContentsTheSame(a: PlatformPrice, b: PlatformPrice) = a == b
    }
}
