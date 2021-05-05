package com.bddi.doomo

import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Data Base Connection : get data
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_nfc, R.id.navigation_library, R.id.account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Hide Action bar
        getSupportActionBar()?.hide()

        val accountButton: Button = findViewById(R.id.account_button)
        accountButton.setOnClickListener {
            navController.navigate(R.id.account)
            navView.uncheckAllItems()
        }


        // Data Base : Get Users informations
        db.collection("Users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    println("${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: "+ exception)
            }

        // Data Base : Get Riri's Story informations
        println("ici")
        val riri = db.collection("Stories").document("B9qEJmMMFYLHESIECbjm")
        riri.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    println("DocumentSnapshot data: ${document.data}")
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: "+ exception)
            }

    }

    /**
     * Uncheck all item from bottom navigation bar
     */
    fun BottomNavigationView.uncheckAllItems() {
        menu.setGroupCheckable(0, true, false)
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        menu.setGroupCheckable(0, true, true)
    }
}