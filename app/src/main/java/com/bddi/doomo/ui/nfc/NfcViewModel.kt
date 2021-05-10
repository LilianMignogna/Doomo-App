package com.bddi.doomo.ui.nfc

import androidx.lifecycle.ViewModel
import com.bddi.doomo.model.Story
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class NfcViewModel : ViewModel() {

    // Data Base Connection : get data
    val db = Firebase.firestore

    /**
     * Get information about a story with the id "storyId" anc call redirection function
     */
    fun getStory(storyId: String, fragment: NfcFragment){
        db.collection("Stories").document(storyId).get()
            .addOnSuccessListener{document ->
                fragment.redirectToStoryDetailsView(document.toObject<Story>()!!)
            }
    }
}
