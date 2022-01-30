package com.example.freedatingclg.ui.fMensajes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freedatingclg.R
import com.example.freedatingclg.databinding.FragmentMensajesBinding
import com.example.freedatingclg.ui.fMensajes.Adapters.ChatAdapter
import com.example.freedatingclg.ui.fMensajes.MensajesViewModel
import com.example.freedatingclg.ui.fMensajes.Models.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class MensajesFragment : Fragment() {

    private val TAG = "CLG-DEBUG"
    // tab 1: https://www.youtube.com/watch?v=Y_DzMXxQB6Y 28:50
    // tab 2: https://www.youtube.com/watch?v=0aOn2mIRlCA 08:00
    // tab 3: https://www.youtube.com/watch?v=dB9JOsVx-yY 03:00
    // tab 4: https://github.com/AlonsoDelCid/MultiChat/blob/master/app/src/main/java/com/alonsodelcid/multichat/adapters/ChatAdapter.kt

    var user: String = ""
    // FirebaseAuth
    private lateinit var auth: FirebaseAuth
    // Database:
    private val db = Firebase.firestore

    /******************************************
    *                OVERRIDES
    *******************************************/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_mensajes, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cogemos el user logado
        auth = FirebaseAuth.getInstance()
        val userr = auth.currentUser?.uid
        val correo = auth.currentUser?.email
        user = "$correo"
        // Solo si tenemos alguien logado inicializamos las vistas
        if (userr != null){
            //println("User logado: $user")
            //Log.i(TAG, "$user está logado")
            initViews(view)
        }
    }

    /******************************************
    *
    ******************************************/

    private fun initViews(vista: View ) {
        // OnClickListener
        val btNewChat: Button? = view?.findViewById(R.id.btNewChat)
        btNewChat?.setOnClickListener {
            newChat(vista)
        }

        // RecyclerView
        val rv: RecyclerView? = vista.findViewById(R.id.rvChats)
        rv?.layoutManager = LinearLayoutManager(activity)
        rv?.adapter = ChatAdapter {
                chat -> chatSelected(chat, vista)
        }

        // Acceso a BBDD
        val userRef = db.collection("users").document(user)
        userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats -> // Del resultado de documentos que me devuelve,
                val listChats = chats.toObjects(Chat::class.java)
                // Casteo el RecyclerView.adapter a ChatAdapter para llamar al método que setea la lista de chats
                (rv?.adapter as ChatAdapter).setData(listChats)
            }

        // Escuchando cambios en tiempo real
        userRef.collection("chats")
            .addSnapshotListener { chats, error ->
                if (error == null){
                    chats?.let {
                        val listChats = it.toObjects(Chat::class.java)
                        // Casteo el RecyclerView.adapter a ChatAdapter para llamar al método que setea la lista de chats
                        (rv?.adapter as ChatAdapter).setData(listChats)
                    }
                }
            }

    }

    private fun newChat(view: View) {

        // OtherUser
        val etNewChat = view.findViewById<EditText>(R.id.etNewChat)// etNewChat
        val otherUser = etNewChat.text.toString()// view.findViewById<EditText>(R.id.etNewChat).text.toString()//

        if(otherUser.isNullOrEmpty()){
            Toast.makeText(activity,"El campo de usuario con el que quieres chatear, está vacío",Toast.LENGTH_LONG)
        }else {
            // Compruebo si el usuario con el que quiere chatear está registrado, si no, saco Toast -- PENDIENTE DE REVISAR, FirebaseDatabase ES UN TRUÑO
            // Añadir comprobación de que no sea el mismo, no termina de ir bien
            val correo = otherUser.replace('.','*',false)
            val dbRef = FirebaseDatabase.getInstance().getReference("Usuarios")
                .child(correo)
                //.orderByChild("correo")
                .child("correo")
                .get().addOnCompleteListener {  // source = https://firebase.google.com/docs/database/android/read-and-write
                    // ID
                    val chatId = UUID.randomUUID().toString() //"randomChatID"

                    // OtherUser
                    // Creado arriba

                    // Lista de usuarios
                    val users = listOf(user, otherUser)

                    // Genero nuevo chat
                    var chat = Chat(chatId, "Chat con $otherUser", users)

                    /**
                     * Alta en FirebaseFirestone en 3 sitios:
                     *
                     * 1. Lista de chats :
                     * 2. Chats del usuario "user" :
                     * 3. Chats del usuario "otherUser" :
                     */
                    // 1
                    Log.i(TAG, "1. meto en chats el chatID: $chatId")
                    db.collection("chats").document(chatId).set(chat)
                    // 2
                    Log.i(TAG, "2. Creo el chat en los chats del user que abre: /users/$user/chat/$chatId")
                    db.collection("users").document(user).collection("chats").document(chatId)
                        .set(chat)
                    // 3
                    Log.i(TAG, "3 Creo el chat en los chats del user que recibe: /users/$otherUser/chat/$chatId")
                    db.collection("users").document(otherUser).collection("chats").document(chatId)
                        .set(chat)
                    /**
                     * https://developer.android.com/guide/navigation/navigation-pass-data#kotlin
                     *
                     * val bundle = bundleOf("amount" to amount)
                     * view.findNavController().navigate(R.id.confirmationAction, bundle)
                     */

                    val bundle =
                        makeBundle(chat) // Mando ChatId y User (usuario logado) para recuperarlo desde el siguiente Fragment
                    view.findNavController()
                        .navigate(R.id.action_nav_mensajes_to_chatFragment, bundle)
                }.addOnFailureListener {
                    Log.e(TAG,"Error, el path Usuario/user@random.com/correo no existe, el resultado es $")
                    // No lo tenemos en la BBDD
                    Toast.makeText(activity,"El usuario no está registrado",Toast.LENGTH_LONG)
                }
        }


    }

    private fun chatSelected(chat: Chat, view: View) {
        /**
         * https://developer.android.com/guide/navigation/navigation-pass-data#kotlin
         *
         * val bundle = bundleOf("amount" to amount)
         * view.findNavController().navigate(R.id.confirmationAction, bundle)
         */
        val bundle = makeBundle(chat)
        view.findNavController().navigate(R.id.action_nav_mensajes_to_chatFragment, bundle)
    }

    private fun makeBundle(chat: Chat): Bundle {
        // https://www.youtube.com/watch?v=ougoYqlPYeY
        val bundle = Bundle()
        bundle.putString("id",chat.id)
        bundle.putString("name",chat.name)
        //bundle.putString("users",chat.users) // Esto es ListOf<String>
        return bundle
    }

}