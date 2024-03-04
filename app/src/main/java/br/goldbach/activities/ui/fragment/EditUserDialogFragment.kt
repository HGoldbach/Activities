package br.goldbach.activities.ui.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import br.goldbach.activities.R
import br.goldbach.activities.databinding.FragmentEditUserDialogBinding
import br.goldbach.activities.ui.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditUserDialogFragment : DialogFragment() {

    private var _binding: FragmentEditUserDialogBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private var imageUri: Uri? = null
    private var showPermission: Boolean = false

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
        Glide.with(this)
            .load(imageUri)
            .into(binding.imageViewUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Dialog_MinWidth)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditUserDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if(!isGranted && !showPermission) {
                    AlertDialog.Builder(activity)
                        .setTitle("Access Image")
                        .setMessage("To select an image you must allow access. Do you want to change your preferences?")
                        .setPositiveButton("Yes") { _,_ ->
                            showPermission = true
                        }
                        .setNegativeButton("Cancel") { _,_ ->
                            return@setNegativeButton
                        }
                        .create()
                        .show()
                } else {
                    Toast.makeText(activity, "Access granted", Toast.LENGTH_SHORT).show()
                }
            }

        getUser()
        onEditButtonClick(requestPermissionLauncher)
        onSaveButtonClick()
        loadImage()
    }

    private fun getUser() {
        homeViewModel.getUser()
    }

    private fun onSaveButtonClick() {
        binding.btnSave.setOnClickListener {
            val name = binding.editTextUser.text
            if (name.isEmpty()) {
                binding.editTextUser.error = "This field is required"
                return@setOnClickListener
            }
            homeViewModel.updateUser(name.toString(), imageUri ?: Uri.EMPTY)
            dismiss()
        }
    }


    private fun onEditButtonClick(requestPermissionLauncher: ActivityResultLauncher<String>) {
        binding.btnEditImage.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES,
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getContent.launch("image/*")
                }

                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> {
                    if(showPermission) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                }
                else -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }
        }
    }

    private fun loadImage() {
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            Glide.with(requireActivity())
                .load(user.imageUri)
                .placeholder(R.drawable.profile)
                .centerCrop()
                .into(binding.imageViewUser)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
