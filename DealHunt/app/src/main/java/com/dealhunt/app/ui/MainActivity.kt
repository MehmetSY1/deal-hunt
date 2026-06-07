package com.dealhunt.app.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dealhunt.app.databinding.ActivityMainBinding
import com.dealhunt.app.model.DealDetail
import com.dealhunt.app.model.GameSearchResult

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var searchAdapter: SearchResultAdapter
    private lateinit var featuredAdapter: FeaturedDealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapters()
        setupSearch()
        observeViewModel()
    }

    private fun setupAdapters() {
        searchAdapter = SearchResultAdapter { game ->
            openGameDetail(game.gameId, game.title, game.thumbnail)
        }

        featuredAdapter = FeaturedDealAdapter { deal ->
            openGameDetail(deal.gameId, deal.title, deal.thumb)
        }

        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = searchAdapter
        }

        binding.rvFeatured.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = featuredAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshFeaturedDeals()
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: ""
                viewModel.searchGames(query)
                binding.btnClear.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })

        binding.btnClear.setOnClickListener {
            binding.etSearch.text?.clear()
            viewModel.clearSearch()
        }
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(this) { state ->
            when (state) {
                is UiState.Idle -> {
                    showFeaturedSection()
                    binding.progressSearch.visibility = View.GONE
                    binding.tvSearchError.visibility = View.GONE
                }
                is UiState.Loading -> {
                    hideAllContent()
                    binding.progressSearch.visibility = View.VISIBLE
                    binding.tvSearchError.visibility = View.GONE
                }
                is UiState.Success -> {
                    hideAllContent()
                    binding.progressSearch.visibility = View.GONE
                    binding.rvSearch.visibility = View.VISIBLE
                    binding.tvSearchError.visibility = View.GONE
                    searchAdapter.submitList(state.data)
                }
                is UiState.Error -> {
                    hideAllContent()
                    binding.progressSearch.visibility = View.GONE
                    binding.tvSearchError.visibility = View.VISIBLE
                    binding.tvSearchError.text = state.message
                }
            }
        }

        viewModel.featuredDeals.observe(this) { state ->
            binding.swipeRefresh.isRefreshing = false
            when (state) {
                is UiState.Loading -> {
                    binding.shimmerFeatured.visibility = View.VISIBLE
                    binding.rvFeatured.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.shimmerFeatured.visibility = View.GONE
                    binding.rvFeatured.visibility = View.VISIBLE
                    featuredAdapter.submitList(state.data)
                }
                is UiState.Error -> {
                    binding.shimmerFeatured.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    private fun showFeaturedSection() {
        binding.layoutFeatured.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE
    }

    private fun hideAllContent() {
        binding.layoutFeatured.visibility = View.GONE
        binding.rvSearch.visibility = View.GONE
    }

    private fun openGameDetail(gameId: String, title: String, thumbnail: String) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra("GAME_ID", gameId)
            putExtra("GAME_TITLE", title)
            putExtra("GAME_THUMB", thumbnail)
        }.also { startActivity(it) }
    }
}
