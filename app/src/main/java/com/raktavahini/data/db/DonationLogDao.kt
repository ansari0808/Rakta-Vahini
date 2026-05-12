package com.raktavahini.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.raktavahini.model.DonationLog

@Dao
interface DonationLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DonationLog): Long

    @Query("SELECT * FROM donation_logs WHERE donorId = :donorId ORDER BY donationDate DESC")
    fun getLogsForDonor(donorId: Int): LiveData<List<DonationLog>>

    @Query("SELECT * FROM donation_logs WHERE donorId = :donorId ORDER BY donationDate DESC LIMIT 1")
    fun getLatestLogForDonor(donorId: Int): LiveData<DonationLog?>

    @Query("SELECT COUNT(*) FROM donation_logs WHERE donorId = :donorId")
    fun getDonationCount(donorId: Int): LiveData<Int>

    @Query("SELECT COUNT(*) FROM donation_logs WHERE donorId = :donorId")
    suspend fun getCountForDonorSync(donorId: Int): Int

    @Query("DELETE FROM donation_logs")
    suspend fun deleteAllLogs()
}
