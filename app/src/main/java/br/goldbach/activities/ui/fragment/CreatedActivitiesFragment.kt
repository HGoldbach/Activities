package br.goldbach.activities.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.goldbach.activities.R
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.databinding.FragmentCreatedActivitiesBinding
import br.goldbach.activities.ui.adapter.ActivitiesAdapter
import br.goldbach.activities.ui.viewmodel.ActivityUiState
import br.goldbach.activities.ui.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatedActivitiesFragment : Fragment() {

    private var _binding: FragmentCreatedActivitiesBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var createdAdapter: ActivitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        createdAdapter = ActivitiesAdapter(homeViewModel, childFragmentManager, requireContext())
        requireActivity().window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor("#00C3F4")
        }

        _binding = FragmentCreatedActivitiesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()
        getCreatedActivities()
        observerCreatedActivities()
        observerUser()
        setUpRecycler()
    }

    private fun getUser() {
        homeViewModel.getUser()
    }

    private fun observerUser() {
        homeViewModel.user.observe(viewLifecycleOwner) {
            binding.textViewUser.text = it?.name
            Glide
                .with(this)
                .load(it?.imageUri)
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.profile)
                .centerCrop()
                .into(binding.imageViewUser)
        }
    }

    private fun getCreatedActivities() {
        homeViewModel.getCreatedActivities()
    }

    private fun setUpRecycler() {
        binding.recyclerViewCreated.apply {
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = createdAdapter
        }
    }

    private fun observerCreatedActivities() {
        homeViewModel.activitiesUiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is ActivityUiState.Success -> {
                    setAdapter(uiState.activities)
                    binding.progressBarCreatedFragment.visibility = View.GONE
                    binding.recyclerViewCreated.visibility = View.VISIBLE
                }

                is ActivityUiState.Loading -> {
                    binding.progressBarCreatedFragment.visibility = View.VISIBLE
                    binding.recyclerViewCreated.visibility = View.GONE
                }

                is ActivityUiState.Error -> Toast.makeText(activity, uiState.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setAdapter(it: List<Activity>) {
        binding.textViewEmptyActivities.visibility =
            if (it.isEmpty()) View.VISIBLE
            else View.GONE
        createdAdapter.update(it, 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}