package com.example.githubuser.ui.home

import android.app.DownloadManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.model.UserData
import com.example.githubuser.network.ApiClient
import com.example.githubuser.network.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class HomeViewModel : ViewModel() {
    val state = MutableLiveData<Map<String, Boolean>>()
    val liveDataUsers = MutableLiveData<ArrayList<UserData>>()
    val users = ArrayList<UserData>()

    fun getUsers(): MutableLiveData<ArrayList<UserData>> {
        return liveDataUsers
    }

    fun setUser(searhQuery: String) {
        state.postValue(mapOf("loading" to true))
        val search = ApiClient.getService().search(searhQuery)
        search.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                state.postValue(mapOf("loading" to false))
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.users.size > 0) {
                            for (i in 0 until it.users.size) {
                                getUserDetail(it.users[i].username)
                            }
                            state.postValue(mapOf("empty" to false))
                        } else {
                            state.postValue(mapOf("empty" to true))
                        }
                    }
                } else {
                    state.postValue(mapOf("error" to true))
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                state.postValue(mapOf("loading" to false))
                state.postValue(mapOf("error" to true))
            }

        })
    }

    fun getUserDetail(username: String) {
        state.postValue(mapOf("loading" to true))
        val getDetail = ApiClient.getService().getDetails(username)
        getDetail.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                state.postValue(mapOf("loading" to false))
                if (response.isSuccessful) {
                    response.body()?.let {
                        users.add(it)
                    }
                    liveDataUsers.postValue(users)
                } else {
                    state.postValue(mapOf("error" to true))
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                state.postValue(mapOf("loading" to false))
                state.postValue(mapOf("error" to true))
            }

        })
    }
}
