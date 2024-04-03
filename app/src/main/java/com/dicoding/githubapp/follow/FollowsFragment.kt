package com.dicoding.githubapp.follow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapp.Utils.Result
import com.dicoding.githubapp.data.response.ItemsItem
import com.dicoding.githubapp.detail.DetailViewModel
import com.dicoding.githubapp.main.UserAdapter
import com.dicoding.githubapp.databinding.FragmentFollowsBinding
import com.dicoding.githubapp.detail.DetailActivity


class FollowsFragment : Fragment() {

    private var binding: FragmentFollowsBinding? = null
    private val adapter by lazy {
        UserAdapter { user ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("item", user)
            }

            requireActivity().startActivity(intent)
        }
    }
    private val viewModel by activityViewModels<DetailViewModel>()
    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollows?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowsFragment.adapter
        }

        when (type) {
            FOLLOWERS -> {
                viewModel.resultFollowers.observe(viewLifecycleOwner, this::manageResultFollows)
            }

            FOLLOWING -> {
                viewModel.resultFollowing.observe(viewLifecycleOwner, this::manageResultFollows)
            }
        }
    }

    private fun manageResultFollows(state: Result) {
        when (state) {
            is Result.Success<*> -> {
                adapter.setData(state.data as MutableList<ItemsItem>)
            }
            is Result.Error -> {
                Toast.makeText(
                    requireActivity(),
                    state.exception.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Result.Loading ->
                binding?.progressBar?.isVisible = state.isLoading
        }
    }

    companion object {
        const val FOLLOWING = 100
        const val FOLLOWERS = 101

        fun newInstance(type: Int) = FollowsFragment()
            .apply {
                this.type = type
            }
    }
}