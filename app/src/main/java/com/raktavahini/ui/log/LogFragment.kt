package com.raktavahini.ui.log

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.raktavahini.R
import com.raktavahini.databinding.FragmentLogBinding

class LogFragment : Fragment() {
    private var _binding: FragmentLogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LogViewModel by viewModels()
    private lateinit var adapter: LogAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LogAdapter()
        binding.rvLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLogs.adapter = adapter

        viewModel.logs.observe(viewLifecycleOwner) { adapter.submitList(it) }
        viewModel.donationCount.observe(viewLifecycleOwner) { binding.tvTotalCount.text = it.toString() }
        viewModel.daysSinceLast.observe(viewLifecycleOwner) { days ->
            binding.tvDaysSince.text = if (days >= 999) "—" else days.toString()
        }

        binding.btnAddLog.setOnClickListener { showAddLogDialog() }
    }

    private fun showAddLogDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_log, null)
        val etCenter = dialogView.findViewById<TextInputEditText>(R.id.etCenterName)
        val etNotes = dialogView.findViewById<TextInputEditText>(R.id.etNotes)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.log_add_title)
            .setView(dialogView)
            .setPositiveButton(R.string.log_save) { _, _ ->
                val center = etCenter?.text?.toString()?.takeIf { it.isNotBlank() } ?: getString(R.string.log_unknown_center)
                val notes = etNotes?.text?.toString() ?: ""
                viewModel.addLog(center, notes = notes)
            }
            .setNegativeButton(R.string.log_cancel, null)
            .show()
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
