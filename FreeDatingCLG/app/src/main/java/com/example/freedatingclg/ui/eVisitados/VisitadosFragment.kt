package com.example.freedatingclg.ui.eVisitados

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.freedatingclg.databinding.FragmentVisitadosBinding
import com.example.freedatingclg.ui.eVisitados.VisitadosViewModel

class VisitadosFragment : Fragment() {

    private lateinit var visitadosViewModel: VisitadosViewModel
    private var _binding: FragmentVisitadosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        visitadosViewModel =
            ViewModelProvider(this).get(VisitadosViewModel::class.java)

        _binding = FragmentVisitadosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textVisitados
        visitadosViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}