package com.example.freedatingclg.ui.gAjustes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.freedatingclg.MainActivity
import com.example.freedatingclg.databinding.FragmentAjustesBinding
import com.example.freedatingclg.ui.gAjustes.AjustesViewModel
import com.google.firebase.auth.FirebaseAuth

class AjustesFragment : Fragment() {

    private lateinit var ajustesVM: AjustesViewModel
    private var _binding: FragmentAjustesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ajustesVM =
            ViewModelProvider(this).get(AjustesViewModel::class.java)

        _binding = FragmentAjustesBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Click listeners
        binding.bLogout.setOnClickListener {
            //Toast.makeText(activity,"Funciona?",Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()

        }

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}