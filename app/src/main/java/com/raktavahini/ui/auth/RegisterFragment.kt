package com.raktavahini.ui.auth

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
import com.raktavahini.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupBloodGroupSpinner()

        binding.btnCreateAccount.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val phone = binding.etPhone.text.toString()
            val city = binding.etCity.text.toString()
            val bloodGroup = binding.spinnerBloodGroup.selectedItem.toString()
            
            if (email.isNotEmpty() && pass.isNotEmpty() && firstName.isNotEmpty() && binding.cbAgree.isChecked) {
                val fullName = "$firstName $lastName".trim()
                viewModel.register(email, pass, fullName, bloodGroup, phone, city)
            } else if (!binding.cbAgree.isChecked) {
                Toast.makeText(requireContext(), "Accept terms", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGoToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        
        binding.btnToggleSignIn.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is AuthViewModel.AuthState.RegisterSuccess -> {
                    // Registration and profile sync successful
                    viewModel.logout()

                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Registration successful! Verification email sent. Please verify and then sign in.", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_register_to_login)
                }
                is AuthViewModel.AuthState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
                else -> binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupBloodGroupSpinner() {
        val bloodGroups = listOf("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bloodGroups)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBloodGroup.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
