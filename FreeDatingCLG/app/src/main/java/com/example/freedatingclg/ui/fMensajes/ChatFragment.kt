package com.example.freedatingclg.ui.fMensajes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freedatingclg.R
import com.example.freedatingclg.ui.fMensajes.Adapters.ChatAdapter
import com.example.freedatingclg.ui.fMensajes.Adapters.MessageAdapter
import com.example.freedatingclg.ui.fMensajes.Models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.Query
import java.util.*

class ChatFragment : Fragment() {

    //val args: MensajesFragmentArgs by navArgs() --> Revisar https://www.youtube.com/watch?v=svxs3dcU0z0

    private lateinit var chatId: String
    private lateinit var chatName: String

    var user: String = ""
    // FirebaseAuth
    private lateinit var auth: FirebaseAuth
    // Database:
    private val db = Firebase.firestore

    /******************************************
     *                OVERRIDES
     *******************************************/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_chat, container, false)

        // fetch arguments: // https://www.youtube.com/watch?v=ougoYqlPYeY
        val args = this.arguments

        chatId = args?.get("id").toString()
        chatName = args?.get("name").toString()

        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Test para probar el SafeArgs, funciona Ok
        //val etTemp = view.findViewById<EditText>(R.id.etMensaje)
        //etTemp.hint = "$chatId $chatName"

        val btAtras = view.findViewById<Button>(R.id.btAtras)
        btAtras.setOnClickListener {
            view.findNavController().navigate(R.id.action_nav_chat_to_nav_mensajes)
        }

        // Cogemos el user logado
        auth = FirebaseAuth.getInstance()
        val userr = auth.currentUser?.uid
        val correo = auth.currentUser?.email
        //user = " $userr"
        user = " $correo"
        // Solo si tenemos alguien logado inicializamos las vistas
        if (chatId.isNotEmpty() && userr != null){
            initViews(view)
        }

    }

    /******************************************
     *
     ******************************************/


    private fun initViews(view: View) {
        // OnClickListener
        val btNewChat: Button? = view?.findViewById(R.id.btEnviar)
        btNewChat?.setOnClickListener {
            sendMessage(view)
        }

        // RecyclerView
        val rv: RecyclerView? = view.findViewById(R.id.rvMessage)
        rv?.layoutManager = LinearLayoutManager(activity)
        rv?.adapter = MessageAdapter(user)

        // Database "jala mensajes"
        val chatRef = db.collection("chats").document(chatId)
        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING) // Para ordenar: orderBy("clave", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = messages.toObjects(Message::class.java)
                (rv?.adapter as MessageAdapter).setData(listMessages)
            }

        // Actualización en tiempo real
        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if (error == null){
                    messages?.let {
                        val listMessages = it.toObjects(Message::class.java)
                        (rv?.adapter as MessageAdapter).setData(listMessages)
                    }
                }
            }
    }

    private fun sendMessage(view: View) {

        // Guardamos en axText && borramos el mensaje anterior
        val etMensaje = view.findViewById<TextView>(R.id.etMensaje)
        val axText = etMensaje.text.toString()
        etMensaje.text = ""

        val message = Message(
            message = axText
            ,from = user
            ,dob = Date()
        )

        // INSERT document
        db.collection("chats").document(chatId).collection("messages").document().set(message)

    }


    private fun showChats(){

    }

}