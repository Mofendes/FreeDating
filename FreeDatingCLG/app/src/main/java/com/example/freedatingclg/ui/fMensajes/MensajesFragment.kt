package com.example.freedatingclg.ui.fMensajes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freedatingclg.R
import com.example.freedatingclg.databinding.FragmentMensajesBinding
import com.example.freedatingclg.ui.fMensajes.Adapters.ChatAdapter
import com.example.freedatingclg.ui.fMensajes.MensajesViewModel
import com.example.freedatingclg.ui.fMensajes.Models.Chat
import com.google.firebase.auth.FirebaseAuth





class MensajesFragment : Fragment() {

    // tab 1: https://www.youtube.com/watch?v=Y_DzMXxQB6Y 28:50
    // tab 2: https://www.youtube.com/watch?v=0aOn2mIRlCA 08:00
    // tab 3: https://www.youtube.com/watch?v=dB9JOsVx-yY 03:00
    // tab 4: https://github.com/AlonsoDelCid/MultiChat/blob/master/app/src/main/java/com/alonsodelcid/multichat/adapters/ChatAdapter.kt

    var user: String? = ""
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_mensajes, container, false)

        // Cogemos el user logado
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser?.uid
        // Solo si tenemos alguien logado inicializamos las vistas
        if (user != null){

            //var rv  = view.findViewById(this,R.id.rvChats)

            var rv: RecyclerView =  view.findViewById(R.id.rvChats);
            println("User logado: $user")
            initViews(rv)
        }

        return view

    }

    private fun initViews(rv: RecyclerView ) {
        // OnClickListener


        // RecyclerView
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = ChatAdapter {
                chat -> chatSelected(chat)
        }

    }

    private fun chatSelected(chat: Chat) {
        //navega al otro fragment pasándole el objeto

    }

    /**
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    */
}