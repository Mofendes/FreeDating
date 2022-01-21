package com.example.freedatingclg.ui.fMensajes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MensajesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is mensajes Fragment"
    }
    val text: LiveData<String> = _text
}