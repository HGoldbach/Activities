package br.goldbach.activities.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import br.goldbach.activities.databinding.FragmentCreatedActivitiesBinding
import br.goldbach.activities.ui.adapter.ActivitiesAdapter
import br.goldbach.activities.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreatedActivitiesFragment : Fragment() {

    private lateinit var binding: FragmentCreatedActivitiesBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var createdAdapter: ActivitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createdAdapter = ActivitiesAdapter(homeViewModel, childFragmentManager)
        requireActivity().window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor("#00C3F4")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatedActivitiesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCreatedActivities()
        observerCreatedActivities()
        observerActivitiesStatus()
        setUpRecycler()
    }

    private fun observerActivitiesStatus() {
       homeViewModel.status.observe(viewLifecycleOwner) {
           val message = if(it.equals("Ongoing")) {
               getString(R.string.change_status_ongoing)
           } else if(it.equals("Deleted")) {
               getString(R.string.deleted_activity)
           } else {
               getString(R.string.error, it)
           }
           Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
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
        homeViewModel.createdActivities.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
    }

    private fun setAdapter(it: List<Activity>) {
        createdAdapter.update(it, 1)
    }

}