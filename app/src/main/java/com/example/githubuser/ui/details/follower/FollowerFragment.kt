package com.example.githubuser.ui.details.follower

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.databinding.FragmentFollowerBinding
import com.example.githubuser.interfaces.UserInterface
import com.example.githubuser.model.UserData
import com.example.githubuser.ui.details.DetailsActivity
import com.example.githubuser.util.hide
import com.example.githubuser.util.show


class FollowerFragment : Fragment(), UserInterface {

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var viewModel: FollowerViewModel
    private lateinit var adapter: FollowerAdapter
    private lateinit var userData: UserData
    private lateinit var followers: ArrayList<UserData>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowerBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        fun newInstance(user: UserData): FollowerFragment {
            val args = Bundle()
            args.putParcelable(DetailsActivity.USER, user)

            val fragment = FollowerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userData = arguments?.getParcelable<UserData>(DetailsActivity.USER) as UserData
        followers = ArrayList()

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[FollowerViewModel::class.java]

        adapter = FollowerAdapter(requireContext())
        adapter.userInterface = this
        binding.recyclerViewFollowerUsers.adapter = adapter

        showFollowers(userData.username)
    }

    private fun showFollowers(username: String) {
        followers.clear()
        viewModel.setFollower(username)
        setViewModel()
    }

    private fun setViewModel() {
        viewModel.getUserData().observe(requireActivity(), { result ->
            if (result != null) {
                followers = result
                adapter.submitList(followers)
                isEmpty(false)
            } else {
                isEmpty(true)
            }
        })
        viewModel.loadingState.observeForever {
            loadingState(it)
        }
        viewModel.errorState.observeForever {
            if (it == true) {
                viewModel.errorState.postValue(false)
            }
        }
    }

    private fun isEmpty(state: Boolean) {
        if (state) {
            binding.textViewEmpty.show()
            binding.recyclerViewFollowerUsers.hide()
        } else {
            binding.textViewEmpty.hide()
            binding.recyclerViewFollowerUsers.show()
        }
    }

    private fun loadingState(state: Boolean) {
        if (state) {
            binding.textViewEmpty.hide()
            binding.progressBarFollower.show()
        } else {
            binding.progressBarFollower.hide()
        }
    }

    override fun onUserClicked(view: View, user: UserData) {
        val intent = Intent(
            requireActivity(), DetailsActivity::class.java
        )
        intent.putExtra(DetailsActivity.USER, userData)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireActivity().startActivity(intent)
    }
}