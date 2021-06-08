package com.bddi.doomo.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.google.android.material.button.MaterialButton

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_account, container, false)

        val soundSwitch = root.findViewById<Switch>(R.id.sound_switch)
        val notificationSwitch = root.findViewById<Switch>(R.id.notification_switch)

        soundSwitch.isChecked = (activity as MainActivity).soundEffectBool
        notificationSwitch.isChecked = (activity as MainActivity).notificationBool

        soundSwitch.setOnClickListener {
            (activity as MainActivity).soundEffectBool = soundSwitch.isChecked
            (activity as MainActivity)?.saveData()
        }

        notificationSwitch.setOnClickListener {
            (activity as MainActivity).notificationBool = notificationSwitch.isChecked
            (activity as MainActivity).saveData()
        }

        val helpRedirectionButton: TextView = root.findViewById(R.id.help)
        helpRedirectionButton.setOnClickListener(){
            root.findNavController().navigate(R.id.action_account_to_help)
        }


        return root
    }
}
