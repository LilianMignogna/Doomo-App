package com.bddi.doomo.ui.nfc

import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.bddi.doomo.model.Story
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class NfcFragment : Fragment() {

    private lateinit var nfcViewModel: NfcViewModel
    private lateinit var nfcAdapter: NfcAdapter

    private lateinit var fabButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nfcViewModel =
            ViewModelProvider(this).get(NfcViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_nfc, container, false)

        // get and disable the big scan button to avoid bug on fragment recreation
        fabButton = activity?.findViewById(R.id.navigation_nfc)!!
        fabButton.isClickable = false

        if (NfcAdapter.getDefaultAdapter(this.context) == null) {
            displayMessage("NFC is not available on this device")
        } else {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this.context)
        }

        val accountButton: ImageButton = root.findViewById(R.id.account_button)
        accountButton.setOnClickListener {
            findNavController().navigate(R.id.child_security)
            (activity as MainActivity).uncheckAllItems()
        }

        return root
    }

    /**
     * Called when the fragment is loaded
     */
    override fun onResume() {
        super.onResume()

        // disable the big scan button to avoid bug on fragment recreation
        fabButton.isClickable = false
        if (NfcAdapter.getDefaultAdapter(this.context) != null) {

            // Check if nfc is disable on the phone and display error message
            if (!nfcAdapter.isEnabled) {
                displayMessage("Veuillez activer le NFC")
            }

            // Enable NFC reading
            nfcAdapter.enableReaderMode(
                activity,
                NfcAdapter.ReaderCallback() {
                    // Get Tag and convert his id to the hexadecimal format
                    val id: ByteArray = it.id
                    val tag = StringBuilder()
                    var currentUser = Firebase.auth.currentUser

                    for (b in id) {
                        val st = String.format("%02X", b)
                        tag.append(st)
                    }

                    println(tag.toString())
                    // Recognize Tag ID
                    when (tag.toString()) {
                        "04BB7254680000" -> {
                            if (currentUser != null) {
                                nfcViewModel.ownStory("story_1", currentUser.uid)
                            }
                            (activity as MainActivity).playSound(R.raw.check)
                            succesfullScan("DZevLTdzAisZUcPX8tup")
                        }
                        "1A902040" -> {
                            if (currentUser != null) {
                                nfcViewModel.ownStory("story_1", currentUser.uid)
                            }
                            (activity as MainActivity).playSound(R.raw.check)
                            succesfullScan("DZevLTdzAisZUcPX8tup")
                        }
                        else -> {
                            displayMessage("Figurine non reconnue")
                        }
                    }
                },
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V or NfcAdapter.FLAG_READER_NFC_BARCODE,
                null
            )
        }
    }

    /**
     * Called when the fragment is not on screen anymore
     */
    override fun onDestroy() {
        super.onDestroy()
        if(NfcAdapter.getDefaultAdapter(this.context) != null) {
            // Disable NFC reading
            nfcAdapter.disableReaderMode(this.activity)
        }
        // enable the big scan button to avoid bug on fragment recreation
        fabButton.isClickable = true
    }

    override fun onPause() {
        super.onPause()
        if(NfcAdapter.getDefaultAdapter(this.context) != null) {
            // Disable NFC reading
            nfcAdapter.disableReaderMode(this.activity)
        }
        // enable the big scan button to avoid bug on fragment recreation
        fabButton.isClickable = true
    }

    private fun displayMessage(message: String) {
        // Error : Can't create handler inside thread that has not called Looper.prepare()
        // There is no thread in the callback function, Toast now run on UI Thread to bypass the error
        activity?.runOnUiThread(Runnable() {
            Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
        })
    }

    fun redirectToStoryDetailsView(story: Story) {
        // Load Story informations
        (activity as MainActivity).currentModel = story

        // Error : Can't create handler inside thread that has not called Looper.prepare()
        // There is no thread in the callback function, Toast now run on UI Thread to bypass the error
        (activity as MainActivity).runOnUiThread(Runnable() {
            (activity as MainActivity).uncheckAllItems()
            findNavController().navigate(R.id.action_global_navigation_story_details)
        })
    }

    fun succesfullScan(storyId: String){
        showScannedLayout()

        val showStoryButton : MaterialButton = activity?.findViewById(R.id.show_story)!!
        showStoryButton.setOnClickListener(){
            nfcViewModel.getStory(storyId, this)

        }

    }

    fun showScanningLayout(){
        (activity as MainActivity).runOnUiThread(Runnable() {
            val scanning_layout: ConstraintLayout = activity?.findViewById(R.id.scanning_layout)!!
            val scanned_layout: ConstraintLayout = activity?.findViewById(R.id.scanned_layout)!!
            scanning_layout.isVisible = true
            scanned_layout.isVisible = false
        })
    }
    fun showScannedLayout(){
        (activity as MainActivity).runOnUiThread(Runnable() {
            val scanning_layout: ConstraintLayout = activity?.findViewById(R.id.scanning_layout)!!
            val scanned_layout: ConstraintLayout = activity?.findViewById(R.id.scanned_layout)!!
            scanning_layout.isVisible = false
            scanned_layout.isVisible = true
        })
    }
}
