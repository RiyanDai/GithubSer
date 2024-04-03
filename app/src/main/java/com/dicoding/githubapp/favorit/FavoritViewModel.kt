package com.dicoding.githubapp.favorit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubapp.data.local.room.DbModul


class FavoritViewModel(private val dbModule: DbModul) : ViewModel() {

    fun getUserFavorite() = dbModule.userDao.loadAll()


    class Factory(private val db: DbModul) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoritViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavoritViewModel(db) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}