package com.raktavahini.ui.search

import android.app.Application
import androidx.lifecycle.*
import com.raktavahini.R
import com.raktavahini.data.db.AppDatabase
import com.raktavahini.data.repository.CloudRepository
import com.raktavahini.data.repository.DonorRepository
import com.raktavahini.model.Donor
import com.raktavahini.utils.DistanceUtils
import com.raktavahini.utils.LocationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class DonorWithDistance(
    val donor: Donor,
    val distanceKm: Double
)

sealed class SearchItem {
    data class Header(val title: String) : SearchItem()
    data class DonorItem(val donorWithDistance: DonorWithDistance) : SearchItem()
}

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DonorRepository
    private val cloudRepository = CloudRepository()
    private val locationHelper = LocationHelper(application)
    
    private val _results = MutableLiveData<List<SearchItem>>(emptyList())
    val results: LiveData<List<SearchItem>> = _results

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentBloodGroup = "All"
    private var currentRadius = 10000.0 // Default to Anywhere

    private val isAnywhere: Boolean
        get() = currentRadius >= 10000.0

    private var searchJob: Job? = null
    
    private var currentUserId: String? = null
    private var currentUserPhone: String? = null
    
    // Use actual user coordinates if available
    private var userLat = 0.0
    private var userLon = 0.0

    init {
        val db = AppDatabase.getInstance(application)
        repository = DonorRepository(db.donorDao(), db.donationLogDao())
        
        // Try to load user's own location to center the search
        viewModelScope.launch {
            repository.currentUser.asFlow().collectLatest { user ->
                if (user != null) {
                    currentUserId = user.firebaseUid
                    currentUserPhone = user.phone
                    if (user.latitude != 0.0) {
                        userLat = user.latitude
                        userLon = user.longitude
                        search()
                    } else {
                        // If DB has 0.0, try getting fresh location
                        refreshCurrentLocation()
                    }
                }
            }
        }
    }

    private fun refreshCurrentLocation() {
        viewModelScope.launch {
            val location = locationHelper.getCurrentLocation()
            location?.let {
                userLat = it.latitude
                userLon = it.longitude
                search()
            }
        }
    }

    fun setBloodGroup(bg: String) {
        currentBloodGroup = bg
        search()
    }

    fun setRadius(radius: Double) {
        currentRadius = radius
        search()
    }

    fun search() {
        searchJob?.cancel()
        _isLoading.value = true
        searchJob = viewModelScope.launch {
            // Listen to real-time updates from Firebase Cloud
            cloudRepository.searchDonorsRealTime(currentBloodGroup)
                .catch { e ->
                    android.util.Log.e("SearchViewModel", "Real-time search error", e)
                    _isLoading.value = false
                }
                .collectLatest { donors ->
                    android.util.Log.d("SearchViewModel", "Real-time update: Received ${donors.size} potential donors from cloud")
                    
                    val sorted = donors
                        .map {
                            val dist = if (userLat == 0.0 && userLon == 0.0 && it.latitude == 0.0 && it.longitude == 0.0) {
                                0.0 
                            } else {
                                DistanceUtils.distanceKm(userLat, userLon, it.latitude, it.longitude)
                            }
                            DonorWithDistance(it, dist)
                        }
                        .filter { 
                            val isSelf = if (currentUserId != null && it.donor.firebaseUid != null) {
                                it.donor.firebaseUid == currentUserId
                            } else {
                                it.donor.phone == currentUserPhone
                            }
                            if (isSelf) return@filter false

                            if (isAnywhere) return@filter true
                            it.distanceKm <= currentRadius
                        }
                        .sortedWith(compareByDescending<DonorWithDistance> { it.donor.isEligibleForSearch }.thenBy { it.distanceKm })

                    val items = mutableListOf<SearchItem>()
                    val eligible = sorted.filter { it.donor.isEligibleForSearch }
                    val ineligible = sorted.filter { !it.donor.isEligibleForSearch }

                    if (eligible.isNotEmpty()) {
                        items.add(SearchItem.Header(getApplication<Application>().getString(R.string.search_eligible_header)))
                        items.addAll(eligible.map { SearchItem.DonorItem(it) })
                    }

                    if (ineligible.isNotEmpty()) {
                        items.add(SearchItem.Header(getApplication<Application>().getString(R.string.search_ineligible_header)))
                        items.addAll(ineligible.map { SearchItem.DonorItem(it) })
                    }
                    
                    android.util.Log.d("SearchViewModel", "Final results: ${items.size}")
                    _results.postValue(items)
                    _isLoading.postValue(false)
                }
        }
    }
}
