package com.bddi.doomo

import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bddi.doomo.model.Story
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Data Base Connection : get data
    val db = Firebase.firestore
    public lateinit var currentModel : Story

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

        // Disable placeholder button
        navView.menu.findItem(R.id.placeholder).isEnabled = false

        // Hide Action bar
        getSupportActionBar()?.hide()

        // Set navigation action to the big scan button
        val scanButton: FloatingActionButton = findViewById(R.id.navigation_nfc)
        scanButton.setOnClickListener() {
            navController.navigate(R.id.navigation_nfc)
            uncheckAllItems()
        }

        val accountButton: Button = findViewById(R.id.account_button)
        accountButton.setOnClickListener {
            navController.navigate(R.id.account)
            uncheckAllItems()
        }
    }

    /**
     * Uncheck all item from bottom navigation bar
     */
    fun uncheckAllItems() {
        val navview: BottomNavigationView = findViewById(R.id.nav_view)
        navview.menu.setGroupCheckable(0, true, false)
        for (i in 0 until navview.menu.size()) {
            navview.menu.getItem(i).isChecked = false
        }
        navview.menu.setGroupCheckable(0, true, true)
    }
}