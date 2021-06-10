package com.bddi.doomo.ui.library

import android.view.View
import androidx.lifecycle.ViewModel
import com.bddi.doomo.model.Story
import com.bddi.doomo.model.User
import com.bddi.doomo.ui.account.AccountFragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class LibraryViewModel : ViewModel() {

    // Data Base Connection : get data
    val db = Firebase.firestore

    val query = db.collection("Story")
    val options = FirestoreRecyclerOptions.Builder<Story>().setQuery(query, Story::class.java)

}