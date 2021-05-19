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
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R

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
        val textView: TextView = root.findViewById(R.id.text_account)

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

        accountViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
                textView.text = it
            }
        )
        return root
    }
}
