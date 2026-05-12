package com.raktavahini.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raktavahini.R
import com.raktavahini.databinding.ItemDonorBinding
import com.raktavahini.databinding.ItemSectionHeaderBinding
import java.util.Locale

class DonorAdapter(
    private val onCallClick: (String) -> Unit
) : ListAdapter<SearchItem, RecyclerView.ViewHolder>(DIFF) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchItem.Header -> TYPE_HEADER
            is SearchItem.DonorItem -> TYPE_DONOR
        }
    }

    class HeaderViewHolder(private val b: ItemSectionHeaderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(header: SearchItem.Header) {
            b.tvSectionTitle.text = header.title
        }
    }

    inner class DonorViewHolder(private val b: ItemDonorBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: DonorWithDistance) {
            val donor = item.donor
            val context = b.root.context
            
            b.tvInitials.text = donor.initials()
            b.tvDonorName.text = donor.name
            b.tvBloodGroup.text = donor.bloodGroup
            b.tvDistance.text = String.format(Locale.getDefault(), context.getString(R.string.donor_distance_format), item.distanceKm)
            b.tvLocation.text = donor.locationName
            
            val days = donor.daysSinceLastDonation
            b.tvDaysAgo.text = if (days >= 999) {
                context.getString(R.string.donor_never_donated)
            } else {
                context.getString(R.string.donor_days_ago, days)
            }
            
            val isEligible = donor.isEligibleForSearch
            
            if (isEligible) {
                b.tvEligibleStatus.text = context.getString(R.string.status_eligible)
                b.dotEligible.isActivated = true
            } else {
                val physicalEligible = donor.isPhysicallyEligible
                if (!physicalEligible) {
                    b.tvEligibleStatus.text = context.getString(R.string.status_eligible_in_days, donor.daysUntilEligible)
                } else {
                    b.tvEligibleStatus.text = context.getString(R.string.status_unavailable)
                }
                b.dotEligible.isActivated = false
            }

            b.btnCall.setOnClickListener { onCallClick(donor.phone) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(ItemSectionHeaderBinding.inflate(inflater, parent, false))
        } else {
            DonorViewHolder(ItemDonorBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item is SearchItem.Header && holder is HeaderViewHolder) {
            holder.bind(item)
        } else if (item is SearchItem.DonorItem && holder is DonorViewHolder) {
            holder.bind(item.donorWithDistance)
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_DONOR = 1

        val DIFF = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(a: SearchItem, b: SearchItem): Boolean {
                if (a is SearchItem.Header && b is SearchItem.Header) return a.title == b.title
                if (a is SearchItem.DonorItem && b is SearchItem.DonorItem) {
                    val ad = a.donorWithDistance.donor
                    val bd = b.donorWithDistance.donor
                    return if (ad.firebaseUid != null && bd.firebaseUid != null) {
                        ad.firebaseUid == bd.firebaseUid
                    } else {
                        ad.phone == bd.phone
                    }
                }
                return false
            }
            override fun areContentsTheSame(a: SearchItem, b: SearchItem) = a == b
        }
    }
}
