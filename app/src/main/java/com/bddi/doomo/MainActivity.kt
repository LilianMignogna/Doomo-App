package com.bddi.doomo

import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
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
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {

    // Data Base Connection : get data
    val db = Firebase.firestore

    // Storage connexion : get images
    val storage = Firebase.storage

    // current story Model : data
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
        supportActionBar?.hide()

        // Set navigation action to the big scan button
        val scanButton: FloatingActionButton = findViewById(R.id.navigation_nfc)
        scanButton.setOnClickListener() {
            playSound(R.raw.clic_btn)
            navController.navigate(R.id.navigation_nfc)
            uncheckAllItems()
        }

        // Set sound to navigation button
        navView.menu.findItem(R.id.navigation_home).setSoundOnMenuItemClicked(R.raw.home)
        navView.menu.findItem(R.id.navigation_library).setSoundOnMenuItemClicked(R.raw.bibli)
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



    // TODO Move Code + create inteface

    fun MenuItem.setSoundOnMenuItemClicked(sound: Int){
        val mp: MediaPlayer = MediaPlayer.create(this@MainActivity, sound)
        setOnMenuItemClickListener() {
            mp.start()
            false
        }
    }

    fun Button.setSoundOnButtonClicked(sound: Int){
        val mp: MediaPlayer = MediaPlayer.create(this@MainActivity, sound)
        setOnClickListener() {
            mp.start()
            false
        }
    }

    fun FloatingActionButton.setSoundOnFABClicked(sound: Int){
        val mp: MediaPlayer = MediaPlayer.create(this@MainActivity, sound)
        setOnClickListener() {
            mp.start()
        }
    }
    fun playSound(sound: Int){
        val mp: MediaPlayer = MediaPlayer.create(this@MainActivity, sound)
        mp.start()
    }
}