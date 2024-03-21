package com.dicoding.githubapp.data.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import com.dicoding.githubapp.R
import com.dicoding.githubapp.data.Utils.Result
import com.dicoding.githubapp.data.follow.FollowsFragment
import com.dicoding.githubapp.data.response.ResponseDetailUser
import com.dicoding.githubapp.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var followersCountTextView: TextView
    private lateinit var followingCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        followersCountTextView = findViewById(R.id.followersCount)
        followingCountTextView = findViewById(R.id.followingCount)

        val username = intent.getStringExtra("username") ?: ""

        viewModel.resultDetail.observe(this) { result ->
            when (result) {
                is Result.Success<*> -> {
                    val user = result.data as ResponseDetailUser

                    binding.image.load(user.avatarUrl)
                    binding.username.text = user.login
                    binding.nama.text = user.name
                    followersCountTextView.text = getString(R.string.followers_count, user.followers)
                    followingCountTextView.text = getString(R.string.following_count, user.following)
                }
                is Result.Error -> {
                    Toast.makeText(this, result.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = result.isLoading
                }
            }
        }
        viewModel.getDetailUser(username)

        val fragments = mutableListOf<Fragment>(
            FollowsFragment.newInstance(FollowsFragment.FOLLOWERS),
            FollowsFragment.newInstance(FollowsFragment.FOLLOWING)
        )

        val titleFragments = mutableListOf<String>(
            getString(R.string.followers), getString(R.string.following)
        )

        val adapter = DetailAdapter(this, fragments)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab, binding.viewpager) { tab, posi ->
            tab.text = titleFragments[posi]
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if (it.position == 0) {
                        viewModel.getFollowers(username)
                    } else {
                        viewModel.getFollowing(username)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewModel.getFollowers(username)

    }
}
