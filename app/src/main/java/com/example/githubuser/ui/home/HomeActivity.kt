package com.example.githubuser.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.HomeAdapter
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityHomeBinding
import com.example.githubuser.interfaces.UserInterface
import com.example.githubuser.model.UserData
import com.example.githubuser.ui.details.DetailsActivity
import com.example.githubuser.util.hide
import com.example.githubuser.util.show

class HomeActivity : AppCompatActivity(), UserInterface {


    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: HomeAdapter
    private lateinit var viewModel: HomeViewModel

    private var users = ArrayList<UserData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]

        adapter = HomeAdapter(this)
        adapter.userInterface = this
        binding.recyclerViewUsers.adapter = adapter

        setViewModel()
    }

    private fun setViewModel() {
        viewModel.getUsers().observe(this, { result ->
            if (result != null && result.size > 0) {
                users = result
                adapter.submitList(users)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.state.observeForever {
            it["loading"]?.let { isLoading ->
                loadingState(isLoading)
            }
            it["empty"]?.let { isEmpty ->
                emptyState(isEmpty)
            }
            it["error"]?.let { isError ->
                if (isError) {
                    viewModel.state.postValue(mapOf("error" to false))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.actionbar_home, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.isNotEmpty()) {
                        users.clear()
                        viewModel.setUser(query)
                        setViewModel()
                        emptyState(false)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun emptyState(state: Boolean) {
        if (state) {
            binding.apply {
                textViewEmpty.show()
                recyclerViewUsers.hide()
            }
        } else {
            binding.apply {
                textViewEmpty.hide()
                recyclerViewUsers.show()
            }
        }
        binding.progressBarHome.hide()
    }

    private fun loadingState(state: Boolean) {
        if (state) {
            binding.progressBarHome.show()
        } else {
            binding.progressBarHome.hide()
        }
    }

    override fun onUserClicked(view: View, userData: UserData) {
        val intent = Intent(
            this, DetailsActivity::class.java
        )
        intent.putExtra(DetailsActivity.USER, userData)
        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}




