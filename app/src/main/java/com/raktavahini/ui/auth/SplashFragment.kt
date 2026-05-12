package com.raktavahini.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.raktavahini.R
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1000) // Small delay for splash experience
                when (state) {
                    is AuthViewModel.AuthState.Authenticated -> {
                        findNavController().navigate(R.id.action_splash_to_home)
                    }
                    is AuthViewModel.AuthState.NeedsProfileSetup -> {
                        val bundle = Bundle().apply { putBoolean("isSetup", true) }
                        findNavController().navigate(R.id.action_splash_to_profile_setup, bundle)
                    }
                    is AuthViewModel.AuthState.Unauthenticated,
                    is AuthViewModel.AuthState.Unverified -> {
                        findNavController().navigate(R.id.action_splash_to_login)
                    }
                    else -> {}
                }
            }
        }
    }
}
