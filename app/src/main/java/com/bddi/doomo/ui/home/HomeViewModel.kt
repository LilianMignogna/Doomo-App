package com.bddi.doomo.ui.home

import androidx.lifecycle.ViewModel
import com.bddi.doomo.model.Story
import com.bddi.doomo.model.User
import com.bddi.doomo.ui.library.LibraryFragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {

    // TODO get favorites stories

    // Data Base Connection : get data
    val db = Firebase.firestore

    val query = db.collection("Story")
    val options = FirestoreRecyclerOptions.Builder<Story>().setQuery(query, Story::class.java)

    fun getUserInfos(user: FirebaseUser){
        db.collection("user").document(user.uid).get()
            .addOnSuccessListener { document ->
                HomeFragment().setUserInfos(document.toObject<User>()!!)
            }
    }
}