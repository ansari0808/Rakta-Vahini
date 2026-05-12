package com.raktavahini.ui.log

import android.app.Application
import androidx.lifecycle.*
import com.raktavahini.data.db.AppDatabase
import com.raktavahini.data.repository.CloudRepository
import com.raktavahini.data.repository.DonorRepository
import com.raktavahini.model.DonationLog
import kotlinx.coroutines.launch

class LogViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DonorRepository
    private val cloudRepository = CloudRepository()
    
    val logs: LiveData<List<DonationLog>>
    
    private val _donationCount = MutableLiveData(0)
    val donationCount: LiveData<Int> = _donationCount

    private val _daysSinceLast = MutableLiveData(0L)
    val daysSinceLast: LiveData<Long> = _daysSinceLast

    init {
        val db = AppDatabase.getInstance(application)
        repository = DonorRepository(db.donorDao(), db.donationLogDao())
        
        logs = repository.currentUser.switchMap { donor ->
            if (donor != null) {
                repository.getLogsForDonor(donor.id)
            } else {
                MutableLiveData(emptyList())
            }
        }
        
        repository.currentUser.observeForever { donor ->
            donor?.let {
                viewModelScope.launch {
                    _donationCount.value = repository.getDonationCountSync(it.id)
                    _daysSinceLast.value = it.daysSinceLastDonation
                }
            }
        }
    }

    fun addLog(centerName: String, notes: String) {
        viewModelScope.launch {
            val donor = repository.currentUser.value ?: return@launch
            val now = System.currentTimeMillis()
            
            // 1. Add the log locally (This also updates lastDonationDate in the Donor table)
            val log = DonationLog(donorId = donor.id, donationDate = now, center = centerName, note = notes)
            val newId = repository.insertLog(log)
            
            // 2. Sync Donor profile to Cloud so others see you are temporarily ineligible
            val updatedDonor = donor.copy(lastDonationDate = now)
            try {
                cloudRepository.uploadProfile(updatedDonor)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // 3. Sync the Log itself to Cloud (using the newly generated local ID)
            try {
                cloudRepository.uploadDonationLog(log.copy(id = newId.toInt()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
