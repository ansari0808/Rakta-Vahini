package com.raktavahini.utils

import java.util.concurrent.TimeUnit

object EligibilityUtils {

    private const val ELIGIBILITY_DAYS = 90L

    /**
     * Returns true if the donor is eligible to donate:
     * (Today - lastDonationDate) > 90 days
     */
    fun isEligible(lastDonationDateMillis: Long): Boolean {
        val daysSince = daysSinceLastDonation(lastDonationDateMillis)
        return daysSince > ELIGIBILITY_DAYS
    }

    fun daysSinceLastDonation(lastDonationDateMillis: Long): Long {
        val diff = System.currentTimeMillis() - lastDonationDateMillis
        return TimeUnit.MILLISECONDS.toDays(diff)
    }

    fun daysUntilEligible(lastDonationDateMillis: Long): Long {
        val daysSince = daysSinceLastDonation(lastDonationDateMillis)
        return maxOf(0, ELIGIBILITY_DAYS - daysSince)
    }
}
