package br.goldbach.activities.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.goldbach.activities.R
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.databinding.FragmentOngoingActivitiesBinding
import br.goldbach.activities.ui.adapter.ActivitiesAdapter
import br.goldbach.activities.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OngoingActivitiesFragment : Fragment() {

    private var _binding: FragmentOngoingActivitiesBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var ongoingAdapter: ActivitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ongoingAdapter = ActivitiesAdapter(homeViewModel, childFragmentManager)
        requireActivity().window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor("#FF6600")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOngoingActivitiesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getOngoingActivities()
        observerOngoingActivities()
        setUpRecycler()
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
        homeViewModel.ongoingActivities.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
    }

    private fun setAdapter(it: List<Activity>) {
        ongoingAdapter.update(it, 2)
    }

    private fun getOngoingActivities() {
        homeViewModel.getOngoingActivities()
    }


}