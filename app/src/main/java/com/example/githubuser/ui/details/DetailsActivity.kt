package com.example.githubuser.ui.details

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.model.UserData
import com.example.githubuser.databinding.ActivityDetailsBinding
import com.google.android.material.tabs.TabLayoutMediator


class DetailsActivity : AppCompatActivity() {

    companion object {
        const val USER = "user"
    }

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var adapter: DetailsAdapter
    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageViewImageDetail.setOnClickListener { onBackPressed() }

        if (!intent.hasExtra(USER)) {
            onBackPressed()
        }

        userData = intent.getParcelableExtra<UserData>(USER) as UserData

        Glide.with(this).load(userData.avatarUrl).into(binding.imageViewImageDetail)
        binding.texViewNameDetail.text = userData.name
        binding.textViewCompanyDetail.text = userData.company?: resources.getString(R.string.No_Company)
        binding.textViewLocationDetail.text = userData.location?: resources.getString(R.string.No_Location)
        binding.textViewRepositoryDetail.text = String.format("%s Repositories", userData.repositories)
        binding.textViewUsernameDetail.text = userData.username

        initViewPager()
    }

    private fun initViewPager() {
        adapter = DetailsAdapter(this, userData)
        binding.viewPagerDetail.adapter = adapter
        binding.viewPagerDetail.offscreenPageLimit = adapter.itemCount

        TabLayoutMediator(
                binding.tabLayoutDetail,
                binding.viewPagerDetail
        ) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.followers)
                1 -> tab.text = resources.getString(R.string.following)
            }
        }.attach()
    }
}