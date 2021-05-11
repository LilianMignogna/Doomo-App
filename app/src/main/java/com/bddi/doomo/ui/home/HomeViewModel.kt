package com.bddi.doomo.ui.home

import androidx.lifecycle.ViewModel
import com.bddi.doomo.model.Story
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {

    // TODO get favorites stories

    // Data Base Connection : get data
    val db = Firebase.firestore

    val query = db.collection("Stories")
    val options = FirestoreRecyclerOptions.Builder<Story>().setQuery(query, Story::class.java)
}