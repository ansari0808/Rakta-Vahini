package com.raktavahini.ui.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raktavahini.R
import com.raktavahini.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: DonorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSpinner()
        setupRadiusChips()
        setupObservers()
        viewModel.search()
    }

    private fun setupRecyclerView() {
        adapter = DonorAdapter { phone ->
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
        }
        binding.rvDonors.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDonors.adapter = adapter
    }

    private fun setupSpinner() {
        val displayBloodGroups = resources.getStringArray(R.array.blood_groups)
        val technicalBloodGroups = listOf("All", "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")
        
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, displayBloodGroups)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBloodGroup.adapter = spinnerAdapter
        binding.spinnerBloodGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                viewModel.setBloodGroup(technicalBloodGroups[pos])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupRadiusChips() {
        // Force selection of "Anywhere" on start
        binding.chipAnywhere.isChecked = true
        viewModel.setRadius(10000.0)

        binding.chip10km.setOnCheckedChangeListener { _, isChecked -> if (isChecked) viewModel.setRadius(10.0) }
        binding.chip20km.setOnCheckedChangeListener { _, isChecked -> if (isChecked) viewModel.setRadius(20.0) }
        binding.chip50km.setOnCheckedChangeListener { _, isChecked -> if (isChecked) viewModel.setRadius(50.0) }
        binding.chipAnywhere.setOnCheckedChangeListener { _, isChecked -> if (isChecked) viewModel.setRadius(10000.0) }
    }

    private fun setupObservers() {
        viewModel.results.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            // Correctly count only the ELIGIBLE donors for the summary text
            val eligibleCount = items.count { 
                it is SearchItem.DonorItem && it.donorWithDistance.donor.isEligibleForSearch 
            }
            binding.tvResultCount.text = resources.getQuantityString(com.raktavahini.R.plurals.search_results_count, eligibleCount, eligibleCount)
            binding.tvEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
