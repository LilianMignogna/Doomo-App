package com.bddi.doomo.ui.story_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class
StoryDetailsViewModel : ViewModel() {

    // Data Base Connection : get data
    val db = Firebase.firestore

    fun setFavorite(field: String, userId: String, status: Boolean){
        db.collection("user").document(userId)
            .update(field, status)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is story_details Fragment"
    }
    val text: LiveData<String> = _text
}