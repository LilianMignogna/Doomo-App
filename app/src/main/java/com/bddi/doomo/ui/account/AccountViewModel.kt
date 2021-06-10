package com.bddi.doomo.ui.account

import android.view.View
import androidx.lifecycle.ViewModel
import com.bddi.doomo.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AccountViewModel : ViewModel() {

    // Data Base Connection : get data
    val db = Firebase.firestore

    fun getUserInfos(user: FirebaseUser, view: View){
        db.collection("user").document(user.uid).get()
            .addOnSuccessListener { document ->
                AccountFragment().setUserInfos(document.toObject<User>()!!, view)
            }
    }
}