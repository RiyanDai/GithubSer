package com.dicoding.githubapp.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailAdapter(
    fA: FragmentActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fA) {
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}
