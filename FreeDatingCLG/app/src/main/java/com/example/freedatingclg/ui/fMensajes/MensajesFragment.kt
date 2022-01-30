package com.example.freedatingclg.ui.fMensajes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freedatingclg.R
import com.example.freedatingclg.databinding.FragmentMensajesBinding
import com.example.freedatingclg.ui.fMensajes.Adapters.ChatAdapter
import com.example.freedatingclg.ui.fMensajes.MensajesViewModel
import com.example.freedatingclg.ui.fMensajes.Models.Chat
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class MensajesFragment : Fragment() {

    // tab 1: https://www.youtube.com/watch?v=Y_DzMXxQB6Y 28:50
    // tab 2: https://www.youtube.com/watch?v=0aOn2mIRlCA 08:00
    // tab 3: https://www.youtube.com/watch?v=dB9JOsVx-yY 03:00
    // tab 4: https://github.com/AlonsoDelCid/MultiChat/blob/master/app/src/main/java/com/alonsodelcid/multichat/adapters/ChatAdapter.kt

    var user: String? = ""
    private lateinit var auth: FirebaseAuth

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
        user = auth.currentUser?.uid

        // Solo si tenemos alguien logado inicializamos las vistas
        if (user != null){
            println("User logado: $user")
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

    }

    private fun newChat(view: View) {
        //val chatId = UUID.randomUUID().toString()
        val chatId = "randomChatID"

        // Falta navigate con putExtra() --> Source: https://www.youtube.com/watch?v=svxs3dcU0z0
        val action = MensajesFragmentDirections.actionNavMensajesToChatFragment() // Ver     android:id="@+id/mobile_navigation"
        Navigation.findNavController(view).navigate(action)

    }

    private fun chatSelected(chat: Chat, view: View) {
        //navega al otro fragment pasándole con putExtra()  --> Source: https://www.youtube.com/watch?v=svxs3dcU0z0
        val action = MensajesFragmentDirections.actionNavMensajesToChatFragment() // Ver     android:id="@+id/mobile_navigation"
        Navigation.findNavController(view).navigate(action)
    }


    /**
    private fun demoRecyclerData(): List<Chat>{

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    */
}