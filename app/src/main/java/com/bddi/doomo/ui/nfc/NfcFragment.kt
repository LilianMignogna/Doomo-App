package com.bddi.doomo.ui.nfc

import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bddi.doomo.R

class NfcFragment : Fragment() {

    private lateinit var nfcViewModel: NfcViewModel
    private lateinit var nfcAdapter: NfcAdapter

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

        nfcAdapter = NfcAdapter.getDefaultAdapter(this.context)
        return root
    }

    /**
     * Called when the fragment is loaded
     */
    override fun onResume() {
        super.onResume()

        // Check if nfc is disable on the phone and display error message
        if (!nfcAdapter.isEnabled) {
            displayMessage("Please enable NFC first")
        }

        // Enable NFC reading
        nfcAdapter.enableReaderMode(
            activity,
            NfcAdapter.ReaderCallback() {
                // Get Tag and convert his id to the hexadecimal format
                val id: ByteArray = it.id
                val tag = StringBuilder()

                for (b in id) {
                    val st = String.format("%02X", b)
                    tag.append(st)
                }

                // Recognize Tag ID
                when (tag.toString()) {
                    "04BB7254680000" -> {
                        displayMessage("Blue tag detected : " + tag)
                    }
                    else -> {
                        displayMessage("Tag : " + tag)
                    }
                }
            },
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V or NfcAdapter.FLAG_READER_NFC_BARCODE,
            null
        )
    }

    /**
     * Called when the fragment is not on screen anymore
     */
    override fun onDestroy() {
        super.onDestroy()
        // Disable NFC reading
        nfcAdapter.disableReaderMode(this.activity)
    }
    
    private fun displayMessage(message: String) {
        // Error : Can't create handler inside thread that has not called Looper.prepare()
        // There is no thread in the callback function, Toast now run on UI Thread to bypass the error
        activity?.runOnUiThread(Runnable() {
            Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
        })
    }
}