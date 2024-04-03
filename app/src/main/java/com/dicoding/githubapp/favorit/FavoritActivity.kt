package com.dicoding.githubapp.favorit

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapp.data.local.room.DbModul
import com.dicoding.githubapp.databinding.ActivityFavoritBinding
import com.dicoding.githubapp.detail.DetailActivity
import com.dicoding.githubapp.main.UserAdapter

class FavoritActivity :  AppCompatActivity() {
    private lateinit var binding: ActivityFavoritBinding
    private val adapter by lazy {
        FavoritAdapter { user ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<FavoritViewModel> {
        FavoritViewModel.Factory(DbModul(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.favoritRv.layoutManager = LinearLayoutManager(this)
        binding.favoritRv.adapter = adapter

        viewModel.getUserFavorite().observe(this) { users ->
            adapter.setData(users)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserFavorite().observe(this) { users ->
            adapter.setData(users)
        }
    }

}