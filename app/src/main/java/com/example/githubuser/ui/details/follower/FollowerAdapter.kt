package com.example.githubuser.ui.details.follower

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.interfaces.UserInterface
import com.example.githubuser.model.UserData

class FollowerAdapter(private val context: Context) :
    ListAdapter<UserData, FollowerAdapter.ViewHolder>(DiffCallback()) {

    var userInterface: UserInterface? = null

    inner class ViewHolder(private val binding: ItemUserBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userData: UserData, userInterface: UserInterface?) {
            Glide.with(binding.root).load(userData.avatarUrl).into(binding.imageViewRootImage)
            binding.apply {
                textViewUsername.text = userData.username
                textViewLocationCompany.text = String.format(
                    "%s, %s", userData.location ?: context.getString(
                        R.string.No_Location
                    ), userData.company ?: context.getString(R.string.No_Company)
                )
                textViewFollowerFollowing.text = String.format(
                    context.getString(R.string.Followers_and_Following),
                    userData.followers,
                    userData.following
                )

                constraintLayoutUserRoot.setOnClickListener {
                    userInterface?.onUserClicked(
                        binding.root, userData
                    )

                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        context
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), userInterface)
    }
}

class DiffCallback : DiffUtil.ItemCallback<UserData>() {
    override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
        return oldItem.username == newItem.username
    }

}