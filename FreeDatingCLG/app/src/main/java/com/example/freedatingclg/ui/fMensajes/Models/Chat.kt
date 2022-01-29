package com.example.freedatingclg.ui.fMensajes.Models

data class Chat(
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList()
)
