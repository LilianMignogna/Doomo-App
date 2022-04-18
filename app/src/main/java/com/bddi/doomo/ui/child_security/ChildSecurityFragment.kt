package com.bddi.doomo.ui.child_security

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
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

        val firstNumber = (0..500).random()
        val secondNumber = (0..500).random()
        val thirdNumber = (0..2).random()
        val result = secondNumber * thirdNumber + firstNumber
        val displayOperation: EditText = root.findViewById(R.id.display_operation)
        displayOperation.setText("${firstNumber} + ${secondNumber} x ${thirdNumber}")
        val accountRedirectionButton: TextView = root.findViewById(R.id.button_validate)

        val firstFieldNumber = root.findViewById<EditText>(R.id.result_first)
        val secondFieldNumber= root.findViewById<EditText>(R.id.result_second)
        val thirdFieldNumber = root.findViewById<EditText>(R.id.result_third)
        val fourthFieldNumber= root.findViewById<EditText>(R.id.result_fourth)

        firstFieldNumber.addTextChangedListener {
            if (firstFieldNumber.text.length == 1)
                secondFieldNumber.requestFocus()
        }
        secondFieldNumber.addTextChangedListener {
            if (secondFieldNumber.text.length == 1)
                thirdFieldNumber.requestFocus()
        }
        thirdFieldNumber.addTextChangedListener {
            if (thirdFieldNumber.text.length == 1)
                fourthFieldNumber.requestFocus()
        }

        accountRedirectionButton.setOnClickListener() {
            val firstEnteredNumber: String = firstFieldNumber.text.toString()
            val secondEnteredNumber: String = secondFieldNumber.text.toString()
            val thirdEnteredNumber: String = thirdFieldNumber.text.toString()
            val fourthEnteredNumber: String = fourthFieldNumber.text.toString()

            if (firstEnteredNumber != "" && secondEnteredNumber != "" && thirdEnteredNumber != "" && fourthEnteredNumber != "") {
                val enteredResult = Integer.parseInt(firstEnteredNumber)*1000 + Integer.parseInt(secondEnteredNumber) * 100 + Integer.parseInt(thirdEnteredNumber)*10 + Integer.parseInt(fourthEnteredNumber)
                if (result == enteredResult) {
                    root.findNavController().navigate(R.id.action_child_security_to_account)
                }
            }
        }

        return root
    }
}