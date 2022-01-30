package com.example.freedatingclg.ui.fMensajes.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.freedatingclg.R
import com.example.freedatingclg.ui.fMensajes.Models.Message

class MessageAdapter(private val user: String): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: List<Message> = emptyList()

    fun setData(list: List<Message>){
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        // Devuelve la vista
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(v)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        // Places the data to the cardView from holder (variables definidas en la inner class, que se crean a partir de la vista)
        val message = messages[position]

        //  Visibilidad en función del origen del mensaje
        if(user == message.from){
            holder.myMessageLayout.visibility = View.VISIBLE
            holder.otherMessageLayout.visibility = View.GONE

            holder.myMessageTextView.text = message.message
        } else {
            holder.myMessageLayout.visibility = View.GONE
            holder.otherMessageLayout.visibility = View.VISIBLE

            holder.othersMessageTextView.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        // Siempre llamo a las variables por el ID del view
            // ConstraintLayouts
        var myMessageLayout: ConstraintLayout
        var otherMessageLayout: ConstraintLayout
            // TextViews
        var myMessageTextView: TextView
        var othersMessageTextView: TextView

        init{
            // ConstraintLayouts
            myMessageLayout =  itemView.findViewById(R.id.myMessageLayout)
            otherMessageLayout = itemView.findViewById(R.id.otherMessageLayout)
            // TextViews
            myMessageTextView = itemView.findViewById(R.id.myMessageTextView)
            othersMessageTextView = itemView.findViewById(R.id.othersMessageTextView)

        }

    }

}
/**

class MessageAdapter(private val user: String): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: List<Message> = emptyList()

    fun setData(list: List<Message>){
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_message,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        if(user == message.from){
            holder.itemView.myMessageLayout.visibility = View.VISIBLE
            holder.itemView.otherMessageLayout.visibility = View.GONE

            holder.itemView.myMessageTextView.text = message.message
        } else {
            holder.itemView.myMessageLayout.visibility = View.GONE
            holder.itemView.otherMessageLayout.visibility = View.VISIBLE

            holder.itemView.othersMessageTextView.text = message.message
        }

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }
}
*/