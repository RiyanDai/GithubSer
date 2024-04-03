package com.dicoding.githubapp.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapp.R
import com.dicoding.githubapp.Utils.Result
import com.dicoding.githubapp.data.response.ItemsItem

import com.dicoding.githubapp.databinding.ActivityMainBinding
import com.dicoding.githubapp.detail.DetailActivity
import com.dicoding.githubapp.favorit.FavoritActivity
import com.dicoding.githubapp.setting.SettingActivity
import com.dicoding.githubapp.setting.SettingPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        UserAdapter{user->
             Intent(this, DetailActivity::class.java).apply {
                 putExtra("item",user)
                 startActivity(this )

             }
        }
    }
    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(SettingPreferences(this))
    }

    private var favoriteMenuItem: MenuItem? = null
    private var isFavoriteIconTinted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTheme().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.adapter = adapter


        viewModel.resultUser.observe(this){
            when(it){
                is Result.Success<*> ->{
                    adapter.setData(it.data as MutableList<ItemsItem>)
                }
                is Result.Error ->{
                    Toast.makeText(this,it.exception.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is Result.Loading ->{
                    binding.progressBar.isVisible = it.isLoading
                }


            }
        }

        viewModel.getUser()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        favoriteMenuItem = menu.findItem(R.id.favorite)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.getUser(query)
                } else {
                    viewModel.getUser()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.getUser()
                } else {
                    viewModel.getUser(newText)
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                item.icon?.setTint(ContextCompat.getColor(this, R.color.red))
                isFavoriteIconTinted = true

                val intent = Intent(this, FavoritActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_search -> {
                return true
            }
            R.id.setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
        if (isFavoriteIconTinted) {
            favoriteMenuItem?.icon?.setTint(ContextCompat.getColor(this, android.R.color.white))
            isFavoriteIconTinted = false
        }
    }



}





