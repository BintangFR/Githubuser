package com.example.githubuser.network

import com.example.githubuser.model.UserData
import com.example.githubuser.util.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("search/users")
    @Headers(API_KEY)
    fun search(
        @Query("q") username: String
    ): Call<ApiResponse>

    @GET("users/{username}")
    @Headers(API_KEY)
    fun getDetails(
        @Path("username") username: String
    ): Call<UserData>

    @GET("users/{username}/followers")
    @Headers(API_KEY)
    fun getFollowers(
        @Path("username") username: String
    ): Call<Array<UserData>>

    @GET("users/{username}/following")
    @Headers(API_KEY)
    fun getFollowing(
        @Path("username") username: String
    ): Call<Array<UserData>>
}