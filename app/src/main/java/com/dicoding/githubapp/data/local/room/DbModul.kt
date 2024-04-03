package com.dicoding.githubapp.data.local.room

import android.content.Context
import androidx.room.Room


class DbModul(private val context: Context) {
    private val db = Room.databaseBuilder(context, GithubDB::class.java, "usergithub.db")
        .allowMainThreadQueries()
        .build()

    val userDao = db.userDao()
}