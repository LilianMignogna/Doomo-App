package com.bddi.doomo.ui.child_security

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bddi.doomo.R

class ChildSecurityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_child_security, container, false)

        val accountRedirectionButton: TextView = root.findViewById(R.id.button_validate)
        accountRedirectionButton.setOnClickListener(){
            root.findNavController().navigate(R.id.action_child_security_to_account)
        }

        return root
    }
}
