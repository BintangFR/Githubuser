package com.example.githubuser.network

import com.example.githubuser.model.UserData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiResponse(
    @Expose
    @SerializedName("total_count") val totalCount: Int,
    @Expose
    @SerializedName("incomplete_result") val incompleteResults: Boolean,
    @Expose
    @SerializedName("items") val users: ArrayList<UserData>

)
