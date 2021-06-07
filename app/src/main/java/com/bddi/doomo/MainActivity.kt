package com.bddi.doomo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bddi.doomo.activity.StoryActivity
import com.bddi.doomo.model.Story
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Data Base Connection : get data
    val db = Firebase.firestore
    public lateinit var currentModel: Story

    public var notificationBool = true
    public var soundEffectBool = false
    public var storyId = " "
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        getSupportActionBar()?.hide()

        // Set navigation action to the big scan button
        val scanButton: FloatingActionButton = findViewById(R.id.navigation_nfc)
        scanButton.setOnClickListener() {
            navController.navigate(R.id.navigation_nfc)
            uncheckAllItems()
        }

        loadData()
    }

    public fun saveStory(storyId: String) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("STORY_ID_KEY", storyId)
        }.apply()

        Toast.makeText(this, "Histoire sauvegardé " + storyId, Toast.LENGTH_SHORT).show()
    }

    public fun saveData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putBoolean("NOTIFICATION_KEY", notificationBool)
            putBoolean("SOUND_KEY", soundEffectBool)
        }.apply()

        Toast.makeText(this, "Données savegardées", Toast.LENGTH_SHORT).show()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        notificationBool = sharedPreferences.getBoolean("NOTIFICATION_KEY", true)
        soundEffectBool = sharedPreferences.getBoolean("SOUND_KEY", false)
        val story = sharedPreferences.getString("STORY_ID_KEY", " ").toString()
        if (story != " ") {
            goToStory(story)
        }
    }

    public fun goToStory(storyId: String) {
        Toast.makeText(this, "story chargées " + storyId, Toast.LENGTH_SHORT).show()
        db.collection("Stories").document(storyId).get()
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
        intent.putExtra("Story", "frog")
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
}
