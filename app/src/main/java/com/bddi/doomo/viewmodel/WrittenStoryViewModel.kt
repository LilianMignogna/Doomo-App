package com.bddi.doomo.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.bddi.doomo.activity.WrittenStoryActivity
import com.bddi.doomo.model.Chapter
import com.bddi.doomo.model.WrittenStory
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class WrittenStoryViewModel : ViewModel()  {

    // Data Base Connection : get data
    val db = Firebase.firestore

    /**
     * Get the WrittenStory with the id "writtenStoryId"
     */
    fun getWrittenStoryInfos(writtenStoryId: String, activity: Activity){
        db.collection("WrittenStory").document(writtenStoryId).get()
            .addOnSuccessListener { document ->
                (activity as WrittenStoryActivity).setWrittenStoryInfos(document.toObject<WrittenStory>()!!)
            }
    }

    /**
     * Get all the chapter from the WrittenStory with the id "writtenStoryId"
     */
    fun getWrittenStoryChapters(writtenStoryId: String, activity: Activity){
        val chapterArray: MutableList<Chapter> = mutableListOf<Chapter>()
        db.collection("WrittenStory/${writtenStoryId}/Chapter").get()
            .addOnSuccessListener { document ->
                    document.forEach{ chapterArray.add(it.toObject<Chapter>()!!)}
                (activity as WrittenStoryActivity).processChapters(chapterArray)
            }
    }
}