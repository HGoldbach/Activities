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
import br.goldbach.activities.databinding.FragmentDoneActivitiesBinding
import br.goldbach.activities.ui.adapter.ActivitiesAdapter
import br.goldbach.activities.ui.viewmodel.ActivityUiState
import br.goldbach.activities.ui.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoneActivitiesFragment : Fragment() {

    private var _binding: FragmentDoneActivitiesBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var doneAdapter: ActivitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        doneAdapter = ActivitiesAdapter(homeViewModel, childFragmentManager, requireContext())
        requireActivity().window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor("#008400")
        }
        _binding = FragmentDoneActivitiesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()
        getDoneActivities()
        observerDoneActivities()
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
        binding.recyclerViewDone.apply {
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = doneAdapter
        }
    }

    private fun observerDoneActivities() {
        homeViewModel.activitiesUiState.observe(viewLifecycleOwner) { uiState ->
            when(uiState) {
                is ActivityUiState.Success -> {
                    setAdapter(uiState.activities)
                    binding.progressBarDoneFragment.visibility = View.GONE
                    binding.recyclerViewDone.visibility = View.VISIBLE
                }
                is ActivityUiState.Loading -> {
                    binding.progressBarDoneFragment.visibility = View.VISIBLE
                    binding.recyclerViewDone.visibility = View.GONE
                }
                is ActivityUiState.Error -> Toast.makeText(activity, uiState.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAdapter(it: List<Activity>) {
        if(it.isEmpty()) {
            binding.textViewEmptyActivities.visibility = View.VISIBLE
        } else {
            binding.textViewEmptyActivities.visibility = View.GONE
            doneAdapter.update(it, 3)
        }
    }

    private fun getDoneActivities() {
        homeViewModel.getDoneActivities()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}