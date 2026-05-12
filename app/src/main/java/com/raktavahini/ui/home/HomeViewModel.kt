package com.raktavahini.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.raktavahini.data.db.AppDatabase
import com.raktavahini.data.repository.CloudRepository
import com.raktavahini.data.repository.DonorRepository
import com.raktavahini.model.Donor
import com.raktavahini.model.DonationLog
import com.raktavahini.utils.DistanceUtils
import com.raktavahini.utils.LocationHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DonorRepository
    private val cloudRepository = CloudRepository()
    private val locationHelper = LocationHelper(application)
    
    val currentUser: LiveData<Donor?>
    val latestLog: LiveData<DonationLog?>
    
    val donationCount: LiveData<Int>
    val nearbyCount = MutableLiveData(0)

    init {
        val db = AppDatabase.getInstance(application)
        repository = DonorRepository(db.donorDao(), db.donationLogDao())
        currentUser = repository.currentUser
        
        latestLog = currentUser.switchMap { donor ->
            if (donor != null) {
                repository.getLatestLog(donor.id)
            } else {
                MutableLiveData(null)
            }
        }
        
        // Robustly update donation count whenever current user changes OR logs are added
        donationCount = currentUser.switchMap { donor ->
            if (donor != null) {
                repository.getDonationCount(donor.id)
            } else {
                MutableLiveData(0)
            }
        }

        // Start collecting real-time nearby count
        viewModelScope.launch {
            cloudRepository.searchDonorsRealTime("All").collectLatest { donors ->
                updateNearbyCount(donors)
            }
        }

        // Also ensure count is updated as soon as currentUser is available
        currentUser.observeForever {
            viewModelScope.launch {
                val currentDonors = cloudRepository.searchDonors("All")
                updateNearbyCount(currentDonors)
            }
        }

        // Periodically refresh location
        refreshLocation()
    }

    private suspend fun updateNearbyCount(donors: List<Donor>) {
        val me = currentUser.value
        val myUid = me?.firebaseUid
        val myPhone = me?.phone

        val count = donors.count { donor ->
            // 1. Basic validation
            if (donor.name.isBlank()) return@count false

            // 2. Only count available donors
            if (!donor.isAvailable) return@count false

            // 3. Don't count yourself
            val isSelf = if (myUid != null && donor.firebaseUid != null) {
                donor.firebaseUid == myUid
            } else {
                donor.phone == myPhone
            }
            if (isSelf) return@count false
            
            // 4. For the Home screen count, we show all available donors 
            // to stay consistent with the "Anywhere" search results.
            true
        }
        nearbyCount.postValue(count)
    }

    fun refreshLocation() {
        viewModelScope.launch {
            val location = locationHelper.getCurrentLocation()
            location?.let { loc ->
                currentUser.value?.let { donor ->
                    val updatedDonor = donor.copy(
                        latitude = loc.latitude,
                        longitude = loc.longitude
                    )
                    repository.updateDonor(updatedDonor)
                    try {
                        cloudRepository.uploadProfile(updatedDonor)
                    } catch (_: Exception) {}
                }
            }
        }
    }

    fun refresh() {
        // Real-time listener and currentUser observer handle updates automatically
    }
}
