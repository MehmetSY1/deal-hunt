package com.dealhunt.app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dealhunt.app.R
import com.dealhunt.app.data.NetworkClient
import com.dealhunt.app.databinding.ActivityDetailBinding
import com.dealhunt.app.model.GameDetailUiState
import com.dealhunt.app.model.PlatformPrice

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var priceAdapter: PlatformPriceAdapter

    private var gameId: String = ""
    private var gameTitle: String = ""
    private var gameThumbnail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameId = intent.getStringExtra("GAME_ID") ?: ""
        gameTitle = intent.getStringExtra("GAME_TITLE") ?: ""
        gameThumbnail = intent.getStringExtra("GAME_THUMB") ?: ""

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        viewModel.loadGameDetail(gameId)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = gameTitle
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        Glide.with(this)
            .load(gameThumbnail)
            .placeholder(R.drawable.placeholder_game)
            .into(binding.ivGameHero)
    }

    private fun setupRecyclerView() {
        priceAdapter = PlatformPriceAdapter { price ->
            val url = NetworkClient.dealUrl(price.dealId)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        binding.rvPrices.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = priceAdapter
        }

        binding.btnRefresh.setOnClickListener {
            viewModel.refreshPrices(gameId)
        }

        binding.btnSteam.setOnClickListener {
            // Opened from best deal button
        }
    }

    private fun observeViewModel() {
        viewModel.gameDetail.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressDetail.visibility = View.VISIBLE
                    binding.contentLayout.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.progressDetail.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    renderDetail(state.data)
                }
                is UiState.Error -> {
                    binding.progressDetail.visibility = View.GONE
                    binding.contentLayout.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = state.message
                }
                else -> {}
            }
        }
    }

    private fun renderDetail(detail: GameDetailUiState) {
        binding.tvGameTitle.text = detail.title

        // Best deal banner
        val best = detail.platformPrices.firstOrNull()
        if (best != null) {
            binding.tvBestPlatform.text = best.storeName
            binding.tvBestPrice.text = if (best.currentPrice == 0.0) "ÜCRETSİZ" else "₺${String.format("%.2f", best.currentPrice)}"

            if (best.originalPrice > best.currentPrice) {
                val discount = best.savingsPercent.toInt()
                binding.tvBestDiscount.text = "-%$discount"
                binding.tvBestDiscount.visibility = View.VISIBLE
                binding.tvBestOriginal.text = "₺${String.format("%.2f", best.originalPrice)}"
                binding.tvBestOriginal.visibility = View.VISIBLE
            } else {
                binding.tvBestDiscount.visibility = View.GONE
                binding.tvBestOriginal.visibility = View.GONE
            }

            binding.btnSteam.setOnClickListener {
                val url = NetworkClient.dealUrl(best.dealId)
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }

        // All platform prices
        priceAdapter.submitList(detail.platformPrices)

        // Cheapest ever
        binding.tvCheapestEver.text = "${detail.cheapestEver} (${detail.cheapestEverDate})"

        // Platform count
        binding.tvPlatformCount.text = "${detail.platformPrices.size} platformda mevcut"
    }
}
