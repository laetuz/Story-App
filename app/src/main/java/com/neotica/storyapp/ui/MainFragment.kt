package com.neotica.storyapp.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.neotica.storyapp.R
import com.neotica.storyapp.databinding.FragmentMainBinding
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.models.LoginPreferences
import com.neotica.storyapp.ui.adapter.MainAdapter
import com.neotica.storyapp.ui.response.Story
import com.neotica.storyapp.ui.viewmodel.MainViewModel
import com.neotica.storyapp.util.Constant.DATA
import com.neotica.storyapp.util.Constant.REQUEST_CODE_PERMISSIONS
import com.neotica.storyapp.util.Constant.REQUIRED_PERMISSIONS
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //insert logic here
        setHasOptionsMenu(true)
        addStory()
        val prefLogin = LoginPreferences(requireContext())
        val token = prefLogin.getToken()
        if (token.isNullOrEmpty()) {
            val action = MainFragmentDirections.actionMainFragmentToLogin()
            findNavController().navigate(action)
        }
        viewModelSetup()
    }

    private fun viewModelSetup() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            Log.d("neotica", "refresh layout")
            val action = findNavController().currentDestination?.id
            action?.let { findNavController().navigate(it) }
        }
        viewModel.stories.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Success -> {
                    setupList(it.data)
                    showLoading(false)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is ApiResult.Error -> {
                    Log.e("STORIES", it.errorMessage)
                    showLoading(false)
                }
                is ApiResult.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun setupList(listStory: List<Story>) {
        val adapterStory =
            MainAdapter(listStory, requireContext(), object : MainAdapter.StoryListener {
                override fun onClick(story: Story) {
                    val image = story.photoUrl
                    val name = story.name
                    val desc = story.description
                    val created = story.createdAt
                    val action = MainFragmentDirections.actionMainFragmentToDetailStoryFragment(
                        image,
                        name,
                        desc,
                        created
                    )
                    findNavController().navigate(action)
                }
            })
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvStory.adapter = adapterStory
        binding.rvStory.layoutManager = layoutManager
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun logout() {
        val prefLogin = LoginPreferences(requireContext())
        prefLogin.clearToken()
        val action = MainFragmentDirections.actionMainFragmentToLogin()
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(context, "Permission not granted.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun addStory() {
        binding.fabMain.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToAddStoryFragment()
            findNavController().navigate(action)
        }
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.stories
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}