package com.raktavahini.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.raktavahini.data.db.AppDatabase
import com.raktavahini.data.repository.CloudRepository
import com.raktavahini.data.repository.DonorRepository
import com.raktavahini.model.Donor
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    private val cloudRepository = CloudRepository()
    private val repository: DonorRepository

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        val db = AppDatabase.getInstance(application)
        repository = DonorRepository(db.donorDao(), db.donationLogDao())
        checkUserStatus()
    }

    fun checkUserStatus() {
        val user = auth.currentUser
        if (user != null) {
            user.reload().addOnCompleteListener {
                if (user.isEmailVerified) {
                    checkProfileExistence(user.uid)
                } else {
                    _authState.value = AuthState.Unverified
                }
            }
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    private fun checkProfileExistence(uid: String) {
        viewModelScope.launch {
            try {
                // 1. Check Cloud first to see what's actually on the server
                val cloudDonor = cloudRepository.getProfile(uid)
                // 2. Check local DB
                val localDonor = repository.getDonorByUid(uid)

                if (cloudDonor != null) {
                    // Profile exists in cloud. If local is missing, sync it down.
                    if (localDonor == null) {
                        repository.insertDonor(cloudDonor)
                        // Also sync logs
                        val logs = cloudRepository.getDonationLogs(uid)
                        repository.insertLogs(logs)
                    }
                    _authState.value = AuthState.Authenticated
                } else if (localDonor != null) {
                    // Local exists but Cloud doesn't (User might have wiped Firebase Firestore)
                    // Auto-restore: Re-upload local profile to Cloud
                    cloudRepository.uploadProfile(localDonor)
                    _authState.value = AuthState.Authenticated
                } else {
                    // Truly no profile anywhere
                    _authState.value = AuthState.NeedsProfileSetup
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Sync error: ${e.message}")
            }
        }
    }

    fun login(email: String, pass: String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    checkProfileExistence(user.uid)
                } else {
                    _authState.value = AuthState.Unverified
                }
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message ?: "Login failed")
            }
    }

    fun register(
        email: String,
        pass: String,
        fullName: String,
        bloodGroup: String,
        phone: String,
        city: String
    ) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    user.sendEmailVerification()
                    
                    // Create and sync profile immediately
                    viewModelScope.launch {
                        val donor = Donor(
                            name = fullName,
                            bloodGroup = bloodGroup,
                            phone = phone,
                            location = city,
                            latitude = 0.0,
                            longitude = 0.0,
                            isAvailable = true,
                            firebaseUid = user.uid
                        )
                        // Save locally
                        repository.insertDonor(donor)
                        // Save to Cloud
                        try {
                            cloudRepository.uploadProfile(donor)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        
                        _authState.value = AuthState.RegisterSuccess
                    }
                } else {
                    _authState.value = AuthState.Error("Registration failed")
                }
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message ?: "Registration failed")
            }
    }

    fun resendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
    }

    /**
     * Forces a full sync between local and cloud data.
     * Useful if data was manually deleted from Firebase console.
     */
    fun syncNow() {
        val user = auth.currentUser
        if (user != null) {
            checkProfileExistence(user.uid)
        }
    }

    fun logout() {
        viewModelScope.launch {
            // We no longer clear local data on logout to preserve donation logs and profile history
            // repository.clearAllData()
            auth.signOut()
            _authState.value = AuthState.Unauthenticated
        }
    }

    sealed class AuthState {
        object Loading : AuthState()
        object Authenticated : AuthState()
        object Unauthenticated : AuthState()
        object Unverified : AuthState()
        object NeedsProfileSetup : AuthState()
        object RegisterSuccess : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
