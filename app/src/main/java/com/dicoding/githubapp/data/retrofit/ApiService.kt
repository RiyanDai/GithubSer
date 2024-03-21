package com.dicoding.githubapp.data.retrofit


import com.dicoding.githubapp.BuildConfig
import com.dicoding.githubapp.data.response.ItemsItem
import com.dicoding.githubapp.data.response.Response
import com.dicoding.githubapp.data.response.ResponseDetailUser

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

     @JvmSuppressWildcards
     @GET("users")
     suspend fun getResponse(
          @Header("Authorization")
          authorization: String = BuildConfig.TOKEN
     ): MutableList<ItemsItem>

     @JvmSuppressWildcards
     @GET("users/{username}")
     suspend fun getDetailUser(
          @Path("username") username: String,
          @Header("Authorization")
          authorization: String = BuildConfig.TOKEN
     ): ResponseDetailUser


     @JvmSuppressWildcards
     @GET("/users/{username}/followers")
     suspend fun getFollowersUser(
          @Path("username") username: String,
          @Header("Authorization")
          authorization: String= BuildConfig.TOKEN
     ): MutableList<ItemsItem>

     @JvmSuppressWildcards
     @GET("/users/{username}/following")
     suspend fun getFollowingUser(
          @Path("username") username: String,
          @Header("Authorization")
          authorization: String= BuildConfig.TOKEN
     ): MutableList<ItemsItem>

     @JvmSuppressWildcards
     @GET("search/users")
     suspend fun searchUser(
          @QueryMap params: Map<String, Any>,
          @Header("Authorization")
          authorization: String = BuildConfig.TOKEN
     ): Response





}