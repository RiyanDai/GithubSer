package com.dicoding.githubapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.githubapp.data.response.ItemsItem


@Database(entities = [ItemsItem::class], version = 1, exportSchema = false)
abstract class GithubDB : RoomDatabase() {
    abstract fun userDao(): UserDao
}