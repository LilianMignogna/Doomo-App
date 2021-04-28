package com.bddi.doomo.ui.nfc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NfcViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is nfc Fragment"
    }
    val text: LiveData<String> = _text
}