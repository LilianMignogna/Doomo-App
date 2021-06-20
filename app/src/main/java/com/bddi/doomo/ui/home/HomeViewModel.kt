package com.bddi.doomo.ui.home

import androidx.lifecycle.ViewModel
import com.bddi.doomo.MainActivity
import com.bddi.doomo.model.Story
import com.bddi.doomo.model.User
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {

    // TODO get favorites stories

    // Data Base Connection : get data
    val db = Firebase.firestore

    private lateinit var query: Query

    lateinit var options: FirestoreRecyclerOptions.Builder<Story>

    fun getStory(){
        if(MainActivity.fav_story_1){
            query = db.collection("Story").whereIn("title", listOf("Riri la grenouille", "Freddy le Gypaète"))
        }
        else{
            query = db.collection("Story").whereNotEqualTo("title", "Riri la grenouille").whereEqualTo("title", "Freddy le Gypaète")
        }
        options = FirestoreRecyclerOptions.Builder<Story>().setQuery(query, Story::class.java)
    }

    fun getUserInfos(user: FirebaseUser){
        db.collection("user").document(user.uid).get()
            .addOnSuccessListener { document ->
                HomeFragment().setUserInfos(document.toObject<User>()!!)
            }
    }
}