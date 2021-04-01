package com.example.githubuser.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
        @Expose
        @SerializedName("id") var id: Int,
        @Expose
        @SerializedName("login") var username: String,
        @Expose
        @SerializedName("name") var name: String?,
        @Expose
        @SerializedName("company") var company: String?,
        @Expose
        @SerializedName("location") var location: String?,
        @Expose
        @SerializedName("bio") var bio: String?,
        @Expose
        @SerializedName("public_repos") var repositories: String?,
        @Expose
        @SerializedName("followers") var followers: String?,
        @Expose
        @SerializedName("following") var following: String?,
        @Expose
        @SerializedName("followers_url") var followersUrl: String?,
        @Expose
        @SerializedName("following_url") var followingUrl: String?,
        @Expose
        @SerializedName("avatar_url") var avatarUrl: String?
) : Parcelable

