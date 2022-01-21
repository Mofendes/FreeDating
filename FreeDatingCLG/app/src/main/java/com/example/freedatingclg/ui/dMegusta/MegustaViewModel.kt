package com.example.freedatingclg.ui.dMegusta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MegustaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Fragmento megusta"
    }
    val text: LiveData<String> = _text
}