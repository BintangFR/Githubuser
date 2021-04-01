package com.example.githubuser.ui.details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.model.UserData
import com.example.githubuser.ui.details.follower.FollowerFragment
import com.example.githubuser.ui.details.following.FollowingFragment

class DetailsAdapter(private val activity: DetailsActivity, private val user: UserData) :
        FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowerFragment.newInstance(user)
            1 -> FollowingFragment.newInstance(user)
            else -> FollowerFragment.newInstance(user)
        }
    }
}