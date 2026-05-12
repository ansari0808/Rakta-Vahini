package com.raktavahini.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "donation_logs")
data class DonationLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val donorId: Int,
    val donationDate: Long,
    val center: String,
    val note: String = ""
) {
    val displayDate: String
        get() = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(donationDate))

    val centerName: String
        get() = center
}
