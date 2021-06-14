package com.bddi.doomo.ui.story_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class
$5StoryDetailsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is story_details Fragment"
    }
    val text: LiveData<String> = _text
}