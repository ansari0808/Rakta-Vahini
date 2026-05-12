package com.raktavahini.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.raktavahini.R
import com.raktavahini.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBloodGroupSpinner()
        setupObservers()
        
        binding.btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun setupBloodGroupSpinner() {
        val bloodGroups = listOf("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bloodGroups)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBloodGroup.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { donor ->
            donor?.let {
                binding.etName.setText(it.name)
                binding.etPhone.setText(it.phone)
                binding.etLocation.setText(it.location)
                
                val bloodGroups = listOf("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")
                val index = bloodGroups.indexOf(it.bloodGroup)
                if (index >= 0) {
                    binding.spinnerBloodGroup.setSelection(index)
                }
            }
        }
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString()
        val phone = binding.etPhone.text.toString()
        val location = binding.etLocation.text.toString()
        val bloodGroup = binding.spinnerBloodGroup.selectedItem.toString()

        if (name.isBlank() || phone.isBlank() || location.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.updateProfile(name, bloodGroup, phone, location)
        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
        
        // Use the isSetup argument or backstack state to decide where to navigate
        val isSetup = arguments?.getBoolean("isSetup") ?: false
        
        if (isSetup || findNavController().previousBackStackEntry == null) {
            findNavController().navigate(R.id.action_edit_to_home)
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
