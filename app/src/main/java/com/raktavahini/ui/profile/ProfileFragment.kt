package com.raktavahini.ui.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.raktavahini.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val authViewModel: com.raktavahini.ui.auth.AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.name.observe(viewLifecycleOwner) {
            binding.tvProfileName.text = it
            binding.tvInitials.text = it.split(" ").take(2).joinToString("") { w -> w[0].uppercaseChar().toString() }
        }
        viewModel.bloodGroup.observe(viewLifecycleOwner) { binding.tvBloodGroup.text = it }
        viewModel.location.observe(viewLifecycleOwner) { binding.tvLocation.text = it }
        viewModel.phone.observe(viewLifecycleOwner) { binding.tvPhone.text = it }
        viewModel.donationCount.observe(viewLifecycleOwner) { binding.tvTotalDonations.text = it.toString() }
        viewModel.isAvailable.observe(viewLifecycleOwner) { available ->
            // Prevent infinite loop by removing listener before updating UI
            binding.switchEligible.setOnCheckedChangeListener(null)
            binding.switchEligible.isChecked = available
            binding.switchEligible.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked != (viewModel.isAvailable.value ?: true)) {
                    viewModel.toggleAvailability()
                }
            }
            
            val days = viewModel.daysSince
            val eligible = viewModel.isEligible
            binding.tvEligibilityStatus.text = if (eligible) {
                if (days >= 999) getString(com.raktavahini.R.string.profile_eligible_never_donated)
                else getString(com.raktavahini.R.string.profile_eligible_status, days)
            } else {
                getString(com.raktavahini.R.string.profile_ineligible_status, 90 - days)
            }
        }
        viewModel.lastDonationDate.observe(viewLifecycleOwner) {
            binding.tvLastDonationDate.text = getString(com.raktavahini.R.string.profile_last_donation_label, it)
        }

        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            if (state is com.raktavahini.ui.auth.AuthViewModel.AuthState.Unauthenticated) {
                findNavController().navigate(com.raktavahini.R.id.action_profile_to_login)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(com.raktavahini.R.id.action_profile_to_edit)
        }
        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
