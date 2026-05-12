package com.raktavahini.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.raktavahini.R
import com.raktavahini.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.login(email, pass)
            } else {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
        
        binding.btnToggleRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is AuthViewModel.AuthState.Authenticated -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_login_to_home)
                }
                is AuthViewModel.AuthState.NeedsProfileSetup -> {
                    binding.progressBar.visibility = View.GONE
                    val bundle = Bundle().apply { putBoolean("isSetup", true) }
                    findNavController().navigate(R.id.action_login_to_profile_setup, bundle)
                }
                is AuthViewModel.AuthState.Unverified -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Please verify your email. Check your inbox.", Toast.LENGTH_LONG).show()
                }
                is AuthViewModel.AuthState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
                else -> binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
