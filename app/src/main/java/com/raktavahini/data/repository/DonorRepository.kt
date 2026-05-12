package com.raktavahini.data.repository

import androidx.lifecycle.LiveData
import com.raktavahini.data.db.DonorDao
import com.raktavahini.data.db.DonationLogDao
import com.raktavahini.model.Donor
import com.raktavahini.model.DonationLog

import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap

class DonorRepository(private val donorDao: DonorDao, private val donationLogDao: DonationLogDao) {

    private val auth = FirebaseAuth.getInstance()
    private val _currentUid = MutableLiveData<String?>(auth.currentUser?.uid)

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUid.postValue(firebaseAuth.currentUser?.uid)
        }
    }

    val currentUser: LiveData<Donor?> = _currentUid.switchMap { uid ->
        if (uid != null) {
            donorDao.getDonorByUidLiveData(uid)
        } else {
            MutableLiveData(null)
        }
    }

    val allDonors: LiveData<List<Donor>> = donorDao.getAllDonors()

    fun getEligibleDonors(bloodGroup: String): LiveData<List<Donor>> {
        return donorDao.getEligibleDonors(bloodGroup)
    }

    suspend fun getDonorByUid(uid: String): Donor? = donorDao.getDonorByUid(uid)

    suspend fun insertDonor(donor: Donor) = donorDao.insertDonor(donor)

    suspend fun updateDonor(donor: Donor) = donorDao.updateDonor(donor)

    suspend fun insertLog(log: DonationLog): Long {
        val id = donationLogDao.insertLog(log)
        donorDao.updateLastDonationDate(log.donorId, log.donationDate)
        return id
    }

    suspend fun insertLogs(logs: List<DonationLog>) {
        logs.forEach { donationLogDao.insertLog(it) }
    }

    fun getDonationCount(donorId: Int) = donationLogDao.getDonationCount(donorId)
    
    suspend fun getDonationCountSync(donorId: Int): Int {
        return donationLogDao.getCountForDonorSync(donorId)
    }

    fun getLatestLog(donorId: Int = 0) = donationLogDao.getLatestLogForDonor(donorId)

    fun getLogsForDonor(donorId: Int) = donationLogDao.getLogsForDonor(donorId)

    suspend fun clearAllData() {
        donorDao.deleteAllDonors()
        donationLogDao.deleteAllLogs()
    }
}
