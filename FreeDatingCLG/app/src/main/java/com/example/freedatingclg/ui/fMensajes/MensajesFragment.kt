package com.example.freedatingclg.ui.fMensajes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.freedatingclg.databinding.FragmentMensajesBinding
import com.example.freedatingclg.ui.fMensajes.MensajesViewModel

class MensajesFragment : Fragment() {

    private lateinit var slideshowViewModel: MensajesViewModel
    private var _binding: FragmentMensajesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(MensajesViewModel::class.java)

        _binding = FragmentMensajesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMensajes
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}