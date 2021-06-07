package com.bddi.doomo.ui.library

import androidx.lifecycle.ViewModel
import com.bddi.doomo.model.Story
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LibraryViewModel : ViewModel() {

    // Data Base Connection : get data
    val db = Firebase.firestore

    val query = db.collection("Story")
    val options = FirestoreRecyclerOptions.Builder<Story>().setQuery(query, Story::class.java)

}