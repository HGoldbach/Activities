package br.goldbach.activities.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.goldbach.activities.R
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.databinding.FragmentOngoingActivitiesBinding
import br.goldbach.activities.ui.adapter.ActivitiesAdapter
import br.goldbach.activities.ui.viewmodel.ActivityUiState
import br.goldbach.activities.ui.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OngoingActivitiesFragment : Fragment(R.layout.fragment_ongoing_activities) {

    private var _binding: FragmentOngoingActivitiesBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var ongoingAdapter: ActivitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        ongoingAdapter = ActivitiesAdapter(homeViewModel, childFragmentManager, requireContext())
        requireActivity().window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor("#FF6600")
        }
        _binding = FragmentOngoingActivitiesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()
        getOngoingActivities()
        observerOngoingActivities()
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

    private fun setUpRecycler() {
        binding.recyclerViewOngoing.apply {
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = ongoingAdapter
        }
    }

    private fun observerOngoingActivities() {
        homeViewModel.activitiesUiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is ActivityUiState.Success -> {
                    setAdapter(uiState.activities)
                    binding.progressBarOngoingFragment.visibility = View.GONE
                    binding.recyclerViewOngoing.visibility = View.VISIBLE
                }

                is ActivityUiState.Loading -> {
                    binding.progressBarOngoingFragment.visibility = View.VISIBLE
                    binding.recyclerViewOngoing.visibility = View.GONE
                }

                is ActivityUiState.Error -> Toast.makeText(activity, uiState.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setAdapter(it: List<Activity>) {
        if(it.isEmpty()) {
            binding.textViewEmptyActivities.visibility = View.VISIBLE
        } else {
            binding.textViewEmptyActivities.visibility = View.GONE
            ongoingAdapter.update(it, 2)
        }
    }

    private fun getOngoingActivities() {
        homeViewModel.getOngoingActivities()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}