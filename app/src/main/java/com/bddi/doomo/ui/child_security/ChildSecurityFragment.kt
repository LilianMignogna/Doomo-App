package com.bddi.doomo.ui.child_security

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bddi.doomo.R

class ChildSecurityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_child_security, container, false)

        val firstNumber = (0..500).random()
        val secondNumber = (0..500).random()
        val thirdNumber = (0..2).random()
        val result = secondNumber * thirdNumber + firstNumber
        val displayOperation: EditText = root.findViewById(R.id.display_operation)
        displayOperation.setText("${firstNumber} + ${secondNumber} x ${thirdNumber}")
        val accountRedirectionButton: TextView = root.findViewById(R.id.button_validate)
        accountRedirectionButton.setOnClickListener(){

            val firstEnteredNumber: String = root.findViewById<EditText>(R.id.result_first).text.toString()
            val secondEnteredNumber: String = root.findViewById<EditText>(R.id.result_second).text.toString()
            val thirdEnteredNumber: String = root.findViewById<EditText>(R.id.result_third).text.toString()
            val fourthEnteredNumber: String = root.findViewById<EditText>(R.id.result_fourth).text.toString()

            if(firstEnteredNumber != "" && secondEnteredNumber != "" && thirdEnteredNumber != "" && fourthEnteredNumber != ""){
                val enteredResult = Integer.parseInt(firstEnteredNumber)*1000 + Integer.parseInt(secondEnteredNumber)*100 + Integer.parseInt(thirdEnteredNumber)*10 + Integer.parseInt(fourthEnteredNumber)
                if(result == enteredResult){
                    root.findNavController().navigate(R.id.action_child_security_to_account)
                }
            }
        }

        return root
    }
}
