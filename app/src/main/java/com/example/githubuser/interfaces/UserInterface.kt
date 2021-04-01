package com.example.githubuser.interfaces

import android.view.View
import com.example.githubuser.model.UserData

interface UserInterface {
    fun onUserClicked(view: View, userdata: UserData)
}