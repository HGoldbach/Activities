package br.goldbach.activities.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import br.goldbach.activities.R
import br.goldbach.activities.databinding.FragmentAddActivityDialogBinding
import br.goldbach.activities.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddActivityDialogFragment : DialogFragment() {

    private var _binding: FragmentAddActivityDialogBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Dialog_MinWidth)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("LIFETAG", "OnCreateView Dialog")
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Dialog_MinWidth)
        _binding = FragmentAddActivityDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addActivity()
        observerInsertActivity()
    }

    private fun observerInsertActivity() {
        homeViewModel.createdStatus.observe(viewLifecycleOwner) {
            val message = if(it == null) {
                resources.getString(R.string.added_activity)
            } else {
                resources.getString(R.string.error, it.message)
            }
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }



    private fun addActivity() {

        binding.saveButton.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()

            if(title.isEmpty()) {
                binding.editTextTitle.error = "This field is required"
                return@setOnClickListener
            }
            if(description.isEmpty()) {
                binding.editTextDescription.error = "This field is required"
                return@setOnClickListener
            }

            homeViewModel.insertActivity(title, description)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LIFETAG", "OnDestroy Dialog")
        _binding = null
    }

}