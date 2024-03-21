package com.dicoding.githubapp.data.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubapp.data.Utils.Result
import com.dicoding.githubapp.data.retrofit.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {

    val resultDetail = MutableLiveData<Result>()
    val resultFollowers = MutableLiveData<Result>()
    val resultFollowing = MutableLiveData<Result>()

    val followersCount = MutableLiveData<Int>()
    val followingCount = MutableLiveData<Int>()



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

}