package Repository


import com.google.firebase.firestore.FirebaseFirestore

object FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun saveBlockchainHash(donationHash: String, transactionHash: String) {
        val data = mapOf(
            "donationHash" to donationHash,
            "transactionHash" to transactionHash,
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        firestore.collection("BlockchainDonations")
            .add(data)
            .addOnSuccessListener {
                println("Saved donation hash: ${it.id}")
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}
