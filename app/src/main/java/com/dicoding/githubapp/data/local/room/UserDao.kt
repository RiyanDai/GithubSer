package com.dicoding.githubapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.githubapp.data.response.ItemsItem


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun Insert(user: ItemsItem)

    @Query("SELECT * FROM User")
    fun loadAll(): LiveData<MutableList<ItemsItem>>

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): ItemsItem

    @Delete
    fun delete(user: ItemsItem)
}