package com.dicoding.githubapp.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dicoding.githubapp.data.response.ItemsItem

import com.dicoding.githubapp.databinding.ItemMainBinding

class UserAdapter(
    private val data: MutableList<ItemsItem> = mutableListOf(),
    private val listener:(ItemsItem)->Unit):

    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    fun setData(data: List<ItemsItem>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsItem) {
            binding.apply {
                image.load(item.avatar_url)
                username.text = item.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }
}