package com.dicoding.githubapp.data.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapp.data.Utils.Result
import com.dicoding.githubapp.data.response.ItemsItem

import com.dicoding.githubapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        UserAdapter{user->
             Intent(this, DetailActivity::class.java).apply {
                 putExtra("username",user.login)
                 startActivity(this )

             }
        }
    }
    private  val viewModel by  viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getUser(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    // Panggil metode untuk mengembalikan daftar ke kondisi semula
                    viewModel.getUser() // Memanggil ulang untuk mendapatkan daftar tanpa filter
                } else {
                    // Panggil metode untuk memfilter daftar dengan teks pencarian baru
                    viewModel.getUser(newText)
                }
                return true
            }
        })


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


}





