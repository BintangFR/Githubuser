package com.example.githubuser.ui.details.following

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentFollowingBinding
import com.example.githubuser.interfaces.UserInterface
import com.example.githubuser.model.UserData
import com.example.githubuser.ui.details.DetailsActivity
import com.example.githubuser.util.hide
import com.example.githubuser.util.show

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowingFragment : Fragment(), UserInterface {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var adapter: FollowingAdapter
    private lateinit var viewModel: FollowingViewModel
    private lateinit var userData: UserData
    private lateinit var followings: ArrayList<UserData>

    companion object {
        fun newInstance(userData: UserData): FollowingFragment {
            val args = Bundle()
            args.putParcelable(DetailsActivity.USER, userData)

            val fragment = FollowingFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userData = arguments?.getParcelable<UserData>(DetailsActivity.USER) as UserData
        followings = ArrayList()

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[FollowingViewModel::class.java]

        adapter = FollowingAdapter(requireContext())
        adapter.userInterface = this
        binding.recyclerViewFollowingUsers.adapter = adapter

        showFollowings(userData.username)
    }

    private fun showFollowings(username: String) {
        followings.clear()
        viewModel.setFollowing(username)
        setViewModel()
    }

    private fun setViewModel() {
        viewModel.getUserData().observe(requireActivity(), { result ->
            if (result != null) {
                followings = result
                adapter.submitList(followings)
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
            binding.recyclerViewFollowingUsers.hide()
        } else {
            binding.textViewEmpty.hide()
            binding.recyclerViewFollowingUsers.show()
        }
    }

    private fun loadingState(state: Boolean) {
        if (state) {
            binding.textViewEmpty.hide()
            binding.progressBarFollowing.show()
        } else {
            binding.progressBarFollowing.hide()
        }
    }

    override fun onUserClicked(view: View, userData: UserData) {
        val intent = Intent(
            requireActivity(), DetailsActivity::class.java
        )
        intent.putExtra(DetailsActivity.USER, userData)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireActivity().startActivity(intent)
    }
}