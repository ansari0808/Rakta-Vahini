package com.raktavahini.ui.log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raktavahini.model.DonationLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.raktavahini.databinding.ItemLogBinding

class LogAdapter : ListAdapter<DonationLog, LogAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(private val b: ItemLogBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(log: DonationLog) {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            b.tvDate.text = sdf.format(Date(log.donationDate))
            b.tvCenter.text = log.center
            // Blood group is not in DonationLog, maybe we need to fetch it, or it's not needed here
            // For now, I'll remove the reference to bloodGroup if it's not in the model
            b.tvNote.text = if (log.note.isNotBlank()) "✓ ${log.note}" else "✓ Thank you logged"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<DonationLog>() {
            override fun areItemsTheSame(a: DonationLog, b: DonationLog) = a.id == b.id
            override fun areContentsTheSame(a: DonationLog, b: DonationLog) = a == b
        }
    }
}
