package com.raktavahini.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.raktavahini.R
import com.raktavahini.databinding.FragmentHomeBinding
import com.raktavahini.ui.theme.ThemeViewModel
import com.raktavahini.model.Donor
import com.raktavahini.model.DonationLog
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
        checkLocationPermission()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            viewModel.refreshLocation()
        } else {
            Toast.makeText(requireContext(), "Location permission needed for distance search", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.refreshLocation()
            }
            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun setupObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { donor ->
            donor?.let {
                val greetingRes = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                    in 0..11 -> R.string.home_greeting_morning
                    in 12..16 -> R.string.home_greeting_afternoon
                    else -> R.string.home_greeting_evening
                }
                binding.tvGreeting.text = getString(greetingRes, it.name)
                binding.tvBloodGroup.text = it.bloodGroup
                
                // Update eligibility chip
                val days = it.daysSinceLastDonation
                if (it.isPhysicallyEligible) {
                    binding.chipEligible.text = getString(R.string.home_eligible_status)
                    binding.chipEligible.setChipBackgroundColorResource(R.color.green_eligible)
                } else {
                    binding.chipEligible.text = getString(R.string.profile_ineligible_status, 90 - days)
                    binding.chipEligible.setChipBackgroundColorResource(R.color.gray_ineligible)
                }
                
                updateLastDonationUi(viewModel.latestLog.value, it)
            }
        }

        viewModel.donationCount.observe(viewLifecycleOwner) {
            binding.tvDonationCount.text = it.toString()
        }
        
        viewModel.nearbyCount.observe(viewLifecycleOwner) {
            binding.tvNearbyCount.text = it.toString()
        }
        
        viewModel.latestLog.observe(viewLifecycleOwner) { log ->
            updateLastDonationUi(log, viewModel.currentUser.value)
        }

        themeViewModel.isDarkMode.observe(viewLifecycleOwner) { isDark ->
            binding.btnThemeToggle.setIconResource(if (isDark) R.drawable.ic_dark_mode else R.drawable.ic_light_mode)
        }
    }

    private fun updateLastDonationUi(log: DonationLog?, donor: Donor?) {
        if (log != null) {
            binding.tvLastDonationDate.text = log.displayDate
            binding.tvLastDonationCenter.text = log.centerName
            val days = donor?.daysSinceLastDonation ?: 0
            binding.tvDaysAgo.text = getString(R.string.donor_days_ago, days)
        } else {
            binding.tvLastDonationDate.text = "—"
            binding.tvLastDonationCenter.text = getString(R.string.home_no_donations)
            binding.tvDaysAgo.text = ""
        }
    }

    private fun setupClickListeners() {
        binding.btnLanguage.setOnClickListener {
            showLanguageDialog()
        }
        binding.btnEmergency.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_search)
        }
        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_profile)
        }
        binding.btnLog.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_log)
        }
        binding.btnThemeToggle.setOnClickListener {
            themeViewModel.toggleTheme()
        }
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "हिंदी (Hindi)", "ಕನ್ನಡ (Kannada)")
        val languageCodes = arrayOf("en", "hi", "kn")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.select_language)
            .setItems(languages) { _, which ->
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCodes[which])
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
