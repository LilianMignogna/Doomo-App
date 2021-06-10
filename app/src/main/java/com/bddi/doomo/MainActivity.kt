package com.bddi.doomo

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bddi.doomo.activity.LoginActivity
import com.bddi.doomo.activity.StoryActivity
import com.bddi.doomo.model.Story
import com.bddi.doomo.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class MainActivity : AppCompatActivity() {

    // Data Base Connection : get data
    val db = Firebase.firestore
    public lateinit var currentModel: Story
    private lateinit var auth: FirebaseAuth

    companion object {
        public var story_1 = false
        public var story_2 = false
    }
    public var notificationBool = true
    public var soundEffectBool = false
    public var storyId = " "
    private lateinit var navController: NavController

    // Storage connexion : get images
    val storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

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

        loadData()
        // Set sound to navigation button
        navView.menu.findItem(R.id.navigation_home).setSoundOnMenuItemClicked(R.raw.home)
        navView.menu.findItem(R.id.navigation_library).setSoundOnMenuItemClicked(R.raw.bibli)

    }

    public fun saveStory(storyId: String) {
//        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.apply {
//            putString("STORY_ID_KEY", storyId)
//        }.apply()
    }

    public fun saveData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putBoolean("NOTIFICATION_KEY", notificationBool)
            putBoolean("SOUND_KEY", soundEffectBool)
        }.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        notificationBool = sharedPreferences.getBoolean("NOTIFICATION_KEY", true)
        soundEffectBool = sharedPreferences.getBoolean("SOUND_KEY", false)
        val story = sharedPreferences.getString("STORY_ID_KEY", " ").toString()
        if (story != " ") {
            // goToStory(story)
        }
    }

    public fun goToStory(storyId: String) {
        db.collection("Story").document(storyId).get()
            .addOnSuccessListener { document ->
                navToStory(document.toObject<Story>()!!)
            }
    }

    public fun navToStory(toObject: Story) {
        currentModel = toObject
        runOnUiThread(
            Runnable() {
                uncheckAllItems()
                navController.navigate(R.id.action_global_navigation_story_details)
            }
        )
    }

    fun startStory(storyId: String) {
        saveStory(storyId)
        val intent = Intent(this, StoryActivity::class.java)
        intent.putExtra("Story", storyId)
        startActivity(intent)
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

    fun MenuItem.setSoundOnMenuItemClicked(sound: Int) {
        val mp: MediaPlayer = MediaPlayer.create(this@MainActivity, sound)
        setOnMenuItemClickListener() {
            if (soundEffectBool == true) {
                mp.start()
            }
            false
        }
    }

    fun Button.setSoundOnButtonClicked(sound: Int) {
        val mp: MediaPlayer = MediaPlayer.create(this@MainActivity, sound)
        setOnClickListener() {
            if (soundEffectBool == true) {
                mp.start()
            }
            false
        }
    }

    fun FloatingActionButton.setSoundOnFABClicked(sound: Int) {
        val mp: MediaPlayer = MediaPlayer.create(this@MainActivity, sound)
        setOnClickListener() {
            if (soundEffectBool == true) {
                mp.start()
            }
        }
    }
    fun playSound(sound: Int) {
        val mp: MediaPlayer = MediaPlayer.create(this@MainActivity, sound)
        if (soundEffectBool == true) {
            mp.start()
        }
    }

    fun logOut() {
        auth.signOut()
        val logoutIntent = Intent(this, LoginActivity::class.java)
        logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(logoutIntent)
    }
}
