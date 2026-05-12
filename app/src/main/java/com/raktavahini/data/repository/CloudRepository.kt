package com.raktavahini.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.raktavahini.model.Donor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CloudRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val donorsCollection = firestore.collection("donors")

    suspend fun uploadProfile(donor: Donor) {
        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        val id = uid ?: donor.phone.ifBlank { "user_${System.currentTimeMillis()}" }
        donorsCollection.document(id).set(donor).await()
    }

    suspend fun getProfile(uid: String): Donor? {
        return try {
            val doc = donorsCollection.document(uid).get().await()
            if (doc.exists()) {
                doc.toObject(Donor::class.java)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun searchDonors(bloodGroup: String): List<Donor> {
        return try {
            val snapshot = donorsCollection.get().await()
            val currentUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

            snapshot.toObjects(Donor::class.java).filter { donor ->
                // Basic validation
                if (donor.name.isBlank() || donor.bloodGroup.isBlank()) return@filter false
                
                val matchesBloodGroup = bloodGroup == "All" || donor.bloodGroup == bloodGroup
                val isAvailable = donor.isAvailable
                val isNotMe = donor.firebaseUid == null || donor.firebaseUid != currentUid
                val isEligible = donor.isPhysicallyEligible
                
                matchesBloodGroup && isAvailable && isNotMe && isEligible
            }
        } catch (e: Exception) {
            android.util.Log.e("CloudRepository", "Search failed", e)
            emptyList()
        }
    }

    /**
     * Real-time search that updates whenever a new donor registers or updates their status.
     */
    fun searchDonorsRealTime(bloodGroup: String): Flow<List<Donor>> = callbackFlow {
        val listener = donorsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                android.util.Log.e("CloudRepository", "Firestore Listener Error: ${error.message}")
                close(error)
                return@addSnapshotListener
            }
            
            val donors = snapshot?.toObjects(Donor::class.java)?.filter { donor ->
                // Basic validation: must have at least a name
                if (donor.name.isBlank()) {
                    android.util.Log.d("CloudRepository", "Filtered out document: Name is blank")
                    return@filter false
                }
                
                val matchesBloodGroup = bloodGroup == "All" || donor.bloodGroup == bloodGroup
                
                // Show ALL registered people as requested, ignoring other filters.
                val result = matchesBloodGroup
                
                if (!result) {
                    android.util.Log.d("CloudRepository", "Filtered out ${donor.name}: blood group mismatch ($bloodGroup vs ${donor.bloodGroup})")
                }
                result
            } ?: emptyList()

            android.util.Log.d("CloudRepository", "Snapshot size: ${snapshot?.size()}, Final donors list: ${donors.size}")
            trySend(donors)
        }
        awaitClose { listener.remove() }
    }

    suspend fun getNearbyDonorCount(): Int {
        return try {
            val snapshot = donorsCollection.get().await()
            snapshot.toObjects(Donor::class.java).count { it.name.isNotBlank() }
        } catch (_: Exception) {
            0
        }
    }

    suspend fun uploadDonationLog(log: com.raktavahini.model.DonationLog) {
        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("users").document(uid).collection("logs")
            .document(log.id.toString()).set(log).await()
    }

    suspend fun getDonationLogs(uid: String): List<com.raktavahini.model.DonationLog> {
        return try {
            val snapshot = firestore.collection("users").document(uid).collection("logs").get().await()
            snapshot.toObjects(com.raktavahini.model.DonationLog::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun getNearbyDonorCountRealTime(): Flow<Int> = callbackFlow {
        val listener = donorsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val donors = snapshot?.toObjects(Donor::class.java) ?: emptyList()
            // Count valid, available donors
            val count = donors.count { it.name.isNotBlank() && it.isAvailable }
            trySend(count)
        }
        awaitClose { listener.remove() }
    }

    /**
     * Helper to populate Firestore with sample donors for testing.
     */
    suspend fun seedSampleDonors() {
        val samples = listOf(
            Donor(name = "Amit Kumar", bloodGroup = "O+", phone = "9880012345", location = "Yelahanka, Bangalore", latitude = 13.1007, longitude = 77.5963, isAvailable = true),
            Donor(name = "Suresh Raina", bloodGroup = "A+", phone = "9880054321", location = "Hebbal, Bangalore", latitude = 13.0354, longitude = 77.5988, isAvailable = true),
            Donor(name = "Megha Rao", bloodGroup = "B-", phone = "9886611223", location = "Indiranagar, Bangalore", latitude = 12.9784, longitude = 77.6408, isAvailable = true),
            Donor(name = "John Doe", bloodGroup = "AB+", phone = "9900112233", location = "Whitefield, Bangalore", latitude = 12.9698, longitude = 77.7499, isAvailable = true)
        )
        samples.forEach { donor ->
            donorsCollection.document("sample_${donor.phone}").set(donor).await()
        }
    }
}
