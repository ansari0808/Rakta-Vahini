package com.raktavahini.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.raktavahini.model.Donor

@Dao
interface DonorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonor(donor: Donor): Long

    @Update
    suspend fun updateDonor(donor: Donor)

    // Get donor by Firebase UID as LiveData
    @Query("SELECT * FROM donors WHERE firebaseUid = :uid LIMIT 1")
    fun getDonorByUidLiveData(uid: String): LiveData<Donor?>

    @Query("SELECT * FROM donors WHERE firebaseUid = :uid LIMIT 1")
    suspend fun getDonorByUid(uid: String): Donor?

    @Query("SELECT * FROM donors ORDER BY name ASC")
    fun getAllDonors(): LiveData<List<Donor>>

    @Query("SELECT * FROM donors WHERE bloodGroup = :bloodGroup AND isAvailable = 1")
    fun getEligibleDonors(bloodGroup: String): LiveData<List<Donor>>

    @Query("UPDATE donors SET lastDonationDate = :date WHERE id = :donorId")
    suspend fun updateLastDonationDate(donorId: Int, date: Long)

    @Query("DELETE FROM donors")
    suspend fun deleteAllDonors()
}
