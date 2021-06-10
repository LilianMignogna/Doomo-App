package com.bddi.doomo.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.bddi.doomo.model.User
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccountFragment : Fragment() {

    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_account, container, false)

        auth = Firebase.auth
        val user = auth.currentUser
        accountViewModel.getUserInfos(user!!, root)

        val soundSwitch = root.findViewById<Switch>(R.id.sound_switch)
        val notificationSwitch = root.findViewById<Switch>(R.id.notification_switch)
        val bLogOut: MaterialButton = root.findViewById(R.id.logout_button)

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

        bLogOut.setOnClickListener{
            (activity as MainActivity).logOut()
        }

        val helpRedirectionButton: TextView = root.findViewById(R.id.help)
        helpRedirectionButton.setOnClickListener(){
            root.findNavController().navigate(R.id.action_account_to_help)
        }


        return root
    }

    fun setUserInfos(user: User, view: View){
        val tvProfileName: TextView = view.findViewById(R.id.profile_name)
        val tvProfileEmail: TextView = view.findViewById(R.id.profile_mail)
        val tvProfileAddress: TextView = view.findViewById(R.id.profile_address)
        tvProfileName.text = user.name
        tvProfileEmail.text = user.email
        tvProfileAddress.text = user.address

    }

}
