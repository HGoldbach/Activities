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
import br.goldbach.activities.databinding.FragmentDoneActivitiesBinding
import br.goldbach.activities.ui.adapter.ActivitiesAdapter
import br.goldbach.activities.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoneActivitiesFragment : Fragment() {

    private var _binding: FragmentDoneActivitiesBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var doneAdapter: ActivitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doneAdapter = ActivitiesAdapter(homeViewModel, childFragmentManager)
        requireActivity().window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor("#008400")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoneActivitiesBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDoneActivities()
        observerDoneActivities()
        setUpRecycler()
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
        homeViewModel.doneActivities.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
    }

    private fun setAdapter(it: List<Activity>) {
        doneAdapter.update(it, 3)
    }

    private fun getDoneActivities() {
        homeViewModel.getDoneActivities()
    }

}