package com.example.freedatingclg.ui.dMegusta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.freedatingclg.databinding.FragmentMegustaBinding
import com.example.freedatingclg.ui.dMegusta.MegustaViewModel


class MegustaFragment : Fragment() {

    private lateinit var megustaViewModel: MegustaViewModel
    private var _binding: FragmentMegustaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        megustaViewModel =
            ViewModelProvider(this).get(MegustaViewModel::class.java)

        _binding = FragmentMegustaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        megustaViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}