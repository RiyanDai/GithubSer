package com.dicoding.githubapp.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.githubapp.Utils.Result
import com.dicoding.githubapp.data.local.room.DbModul
import com.dicoding.githubapp.data.response.ItemsItem
import com.dicoding.githubapp.data.retrofit.ApiClient
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(private val db: DbModul): ViewModel() {

    val resultDetail = MutableLiveData<Result>()
    val resultFollowers = MutableLiveData<Result>()
    val resultFollowing = MutableLiveData<Result>()
    val resultSuksesFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()


    fun getDetailUser(username:String){
        viewModelScope.launch{
                flow {
                    val response = ApiClient
                        .apiService
                        .getDetailUser(username)
                    emit(response)
                }.onStart {
                    resultDetail.value = Result.Loading(true)
                }.onCompletion {
                    resultDetail.value = Result.Loading(false)
                }.catch {

                    Log.e("Error", it.message.toString())
                    it.printStackTrace()
                    resultDetail.value = Result.Error(it)
                }.collect{
                    resultDetail.value = Result.Success(it)
                }
            }


    }

    fun getFollowers(username: String){
        viewModelScope.launch{
            flow {
                val response = ApiClient
                    .apiService
                    .getFollowersUser(username)
                emit(response)
            }.onStart {
                resultFollowers.value = Result.Loading(true)
            }.onCompletion {
                resultFollowers.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                resultFollowers.value = Result.Error(it)
            }.collect{
                resultFollowers.value = Result.Success(it)
            }
        }
    }


    fun getFollowing(username: String){
        viewModelScope.launch{
            flow {
                val response = ApiClient
                    .apiService
                    .getFollowingUser(username)
                emit(response)
            }.onStart {
                resultFollowing.value = Result.Loading(true)
            }.onCompletion {
                resultFollowing.value = Result.Loading(false)
            }.catch {

                it.printStackTrace()
                resultFollowing.value = Result.Error(it)
            }.collect{
                resultFollowing.value = Result.Success(it)
            }
        }
    }

    private var isFavorite = false
    fun setFavorite(item: ItemsItem?) {
        viewModelScope.launch {
            item?.let {
                if (isFavorite) {
                    db.userDao.delete(item)
                    resultDeleteFavorite.value = true
                } else {
                    db.userDao.Insert(item)
                    resultSuksesFavorite.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }
    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user != null) {
                listenFavorite()
                isFavorite = true
            }
        }
    }

    class Factory(private val db: DbModul) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }

}