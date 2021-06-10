package com.bddi.doomo.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bddi.doomo.R

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_help, container, false)

        val backArrowButton: ImageButton = root.findViewById(R.id.help_page_title)
        backArrowButton.setOnClickListener(){
            root.findNavController().navigate(R.id.action_help_to_account)
        }

        return root
    }
}
