package com.example.githubuser.ui.details.following

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.model.UserData
import com.example.githubuser.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    val loadingState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<Boolean>()
    val liveDataUser = MutableLiveData<ArrayList<UserData>>()
    val list = ArrayList<UserData>()

    fun getUserData(): MutableLiveData<ArrayList<UserData>> {
        return liveDataUser
    }

    fun setFollowing(username: String) {
        loadingState.postValue(true)
        val follower = ApiClient.getService().getFollowing(username)
        follower.enqueue(object : Callback<Array<UserData>> {
            override fun onResponse(call: Call<Array<UserData>>, response: Response<Array<UserData>>) {
                loadingState.postValue(false)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isNotEmpty()) {
                            for (element in it) {
                                getUserDetailData(element.username)
                            }
                        }
                    }
                } else {
                    errorState.postValue(true)
                }
            }

            override fun onFailure(call: Call<Array<UserData>>, t: Throwable) {
                loadingState.postValue(false)
                errorState.postValue(true)
            }
        })
    }

    fun getUserDetailData(username: String) {
        loadingState.postValue(true)
        val userDetail = ApiClient.getService().getDetails(username)
        userDetail.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                loadingState.postValue(false)
                if (response.isSuccessful) {
                    response.body()?.let {
                        list.add(it)
                        liveDataUser.postValue(list)
                    }
                } else {
                    errorState.postValue(true)
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                loadingState.postValue(false)
                errorState.postValue(true)
            }
        })
    }
}