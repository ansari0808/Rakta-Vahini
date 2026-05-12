package com.raktavahini.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "donors")
data class Donor(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var bloodGroup: String = "",
    var phone: String = "",
    var location: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var lastDonationDate: Long = 0L,
    
    @get:PropertyName("available")
    @set:PropertyName("available")
    var isAvailable: Boolean = true,

    var firebaseUid: String? = null,
) {
    /**
     * Physical eligibility based on the 90-day medical rule.
     */
    @get:Exclude
    val isPhysicallyEligible: Boolean
        get() = (daysSinceLastDonation >= 90) || lastDonationDate == 0L

    /**
     * Combined eligibility: Medically allowed AND user has marked themselves as available.
     * This is what other users see in search results.
     */
    @get:Exclude
    val isEligibleForSearch: Boolean
        get() = isAvailable && isPhysicallyEligible

    @Exclude
    fun initials(): String {
        return name.split(" ")
            .asSequence()
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.firstOrNull()?.uppercase() ?: "" }
    }

    @get:Exclude
    val daysSinceLastDonation: Long
        get() {
            if (lastDonationDate == 0L) return 999 // Never donated
            val diff = System.currentTimeMillis() - lastDonationDate
            return diff / (24 * 60 * 60 * 1000)
        }

    @get:Exclude
    val daysUntilEligible: Long
        get() = (90 - daysSinceLastDonation).coerceAtLeast(0)

    @get:Exclude
    val locationName: String
        get() = location
}
