package com.example.freedatingclg.ui.fMensajes.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freedatingclg.R
import com.example.freedatingclg.ui.fMensajes.Models.Chat

/**
Source: https://www.youtube.com/watch?v=Y_DzMXxQB6Y
*/
//class ChatAdapter(val chatClick: (Chat) -> Unit): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
class ChatAdapter(val chatClick: (Chat) -> Unit): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){

    var chats: List<Chat> = emptyList()

    fun setData(list: List<Chat>){
        chats = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder { // Revisar en fragments
        // Devuelve la vista
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        // Places the data to the cardView from holder (variables definidas en la inner class, que se crean a partir de la vista)
        holder.chatNameText.text = chats[position].name
        holder.usersTextView.text = chats[position].users.toString()

        // Añadir OnClickListener del navigation
        holder.itemView.setOnClickListener { // Click de toda la vista??
            chatClick(chats[position])
        }
    }

    override fun getItemCount(): Int {
        // Devuelve la cantidad de elementos, no tiene perdida
        return chats.size
    }

    inner class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var chatNameText: TextView
        var usersTextView: TextView

        init{
            itemImage = itemView.findViewById(R.id.ivFotoCard)
            chatNameText = itemView.findViewById(R.id.chatNameText)
            usersTextView = itemView.findViewById(R.id.usersTextView)
        }

    }

    //  class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
