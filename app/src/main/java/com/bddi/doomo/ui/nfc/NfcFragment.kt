package com.bddi.doomo.ui.nfc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bddi.doomo.R

class NfcFragment : Fragment() {

    private lateinit var nfcViewModel: NfcViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        nfcViewModel =
                ViewModelProvider(this).get(NfcViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_nfc, container, false)
        val textView: TextView = root.findViewById(R.id.text_nfc)
        nfcViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}