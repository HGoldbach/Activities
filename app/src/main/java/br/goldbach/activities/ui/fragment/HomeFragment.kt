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
import br.goldbach.activities.R
import br.goldbach.activities.data.model.ActivityStats
import br.goldbach.activities.data.model.ActivityStatus
import br.goldbach.activities.databinding.FragmentHomeBinding
import br.goldbach.activities.ui.viewmodel.ActivityUiState
import br.goldbach.activities.ui.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireActivity().window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor("#00C3F4")
        }

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()
        loadStats()
        observerUser()
        observerStats()
        addActivity()
        editUser()
    }

    private fun getUser() {
        homeViewModel.getUser()

    }

    private fun editUser() {
        binding.imageViewEdit.setOnClickListener {
            val dialog = EditUserDialogFragment()
            dialog.show(childFragmentManager, "EditUserDialog")
        }
    }



    private fun observerUser() {
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.textViewUser.text = user.name
            Glide.with(requireActivity())
                .load(user.imageUri)
                .placeholder(R.drawable.profile)
                .centerCrop()
                .into(binding.imageViewUser)
        }
    }


    private fun loadStats() {
        homeViewModel.getActivityStats()
    }

    private fun observerStats() {
        homeViewModel.activitiesUiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is ActivityUiState.Success -> {
                    setUi(uiState.activitiesStats)
                }

                is ActivityUiState.Loading -> {
                    binding.progressBarHomeFragment.visibility = View.VISIBLE
                    binding.activitiesSummaryLayout.visibility = View.GONE
                }

                is ActivityUiState.Error -> Toast.makeText(activity, "Error", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setUi(activitiesStats: List<ActivityStats>) {
        binding.progressBarHomeFragment.visibility = View.GONE
        binding.activitiesSummaryLayout.visibility = View.VISIBLE
        var total = 0
        for (stats in activitiesStats) {
            total += stats.total
            when (stats.status) {
                ActivityStatus.CREATED -> binding.textViewTotalCreatedNumber.text =
                    stats.total.toString()

                ActivityStatus.ONGOING -> binding.textViewTotalOngoingNumber.text =
                    stats.total.toString()

                ActivityStatus.DONE -> binding.textViewTotalDoneNumber.text = stats.total.toString()
            }
        }
        binding.textViewNumberTotal.text = total.toString()
    }

    private fun addActivity() {
        binding.addButton.setOnClickListener {
            AddActivityDialogFragment().show(childFragmentManager, "AddActivityDialogFragment")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}