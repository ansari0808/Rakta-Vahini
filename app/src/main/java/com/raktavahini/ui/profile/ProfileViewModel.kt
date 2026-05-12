package com.raktavahini.ui.profile

import android.app.Application
import androidx.lifecycle.*
import com.raktavahini.data.db.AppDatabase
import com.raktavahini.data.repository.CloudRepository
import com.raktavahini.data.repository.DonorRepository
import com.raktavahini.model.Donor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DonorRepository
    private val cloudRepository = CloudRepository()
    
    val currentUser: LiveData<Donor?>
    
    val name: LiveData<String>
    val bloodGroup: LiveData<String>
    val location: LiveData<String>
    val phone: LiveData<String>
    val isAvailable: LiveData<Boolean>
    val donationCount: LiveData<Int>
    
    val daysSince: Long
        get() = currentUser.value?.daysSinceLastDonation ?: 0
        
    val isEligible: Boolean
        get() = currentUser.value?.isPhysicallyEligible ?: false

    val lastDonationDate: LiveData<String>

    init {
        val db = AppDatabase.getInstance(application)
        repository = DonorRepository(db.donorDao(), db.donationLogDao())
        currentUser = repository.currentUser
        
        name = currentUser.map { it?.name ?: "—" }
        bloodGroup = currentUser.map { it?.bloodGroup ?: "—" }
        location = currentUser.map { it?.locationName ?: "—" }
        phone = currentUser.map { it?.phone ?: "—" }
        isAvailable = currentUser.map { it?.isAvailable ?: true }
        
        lastDonationDate = currentUser.map { donor ->
            if (donor != null && donor.lastDonationDate != 0L) {
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(donor.lastDonationDate))
            } else {
                "—"
            }
        }

        // Live donation count
        donationCount = currentUser.switchMap { donor ->
            if (donor != null) {
                repository.getDonationCount(donor.id)
            } else {
                MutableLiveData(0)
            }
        }
    }

    fun toggleAvailability() {
        viewModelScope.launch {
            currentUser.value?.let { donor ->
                val updatedDonor = donor.copy(isAvailable = !donor.isAvailable)
                repository.updateDonor(updatedDonor)
                
                // Sync to Cloud so others see the availability change
                try {
                    cloudRepository.uploadProfile(updatedDonor)
                } catch (_: Exception) { }
            }
        }
    }

    fun updateProfile(
        name: String,
        bloodGroup: String,
        phone: String,
        location: String,
        lat: Double = 0.0,
        lon: Double = 0.0
    ) {
        viewModelScope.launch {
            val donor = currentUser.value
            
            // If coordinates are 0.0 (default), try to use demo coordinates for Yelahanka 
            // if the location string matches, so they show up in search.
            val finalLat = if (lat == 0.0 && location.contains("Yelahanka", true)) 13.1007 else lat
            val finalLon = if (lon == 0.0 && location.contains("Yelahanka", true)) 77.5963 else lon

            val updatedDonor = donor?.copy(
                name = name,
                bloodGroup = bloodGroup,
                phone = phone,
                location = location,
                latitude = if (finalLat != 0.0) finalLat else donor.latitude,
                longitude = if (finalLon != 0.0) finalLon else donor.longitude,
                firebaseUid = donor.firebaseUid ?: com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
            ) ?: Donor(
                name = name,
                bloodGroup = bloodGroup,
                phone = phone,
                location = location,
                latitude = finalLat,
                longitude = finalLon,
                firebaseUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
            )

            if (donor != null) {
                repository.updateDonor(updatedDonor)
            } else {
                repository.insertDonor(updatedDonor)
            }
            
            // Sync to Cloud
            try { 
                cloudRepository.uploadProfile(updatedDonor) 
            } catch (_: Exception) { }
        }
    }
}
