package com.bddi.doomo.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.bddi.doomo.ui.story.InteractButtonFragment
import com.bddi.doomo.ui.story.VideoFragment

class StoryActivity : AppCompatActivity() {

    var storyFragment = intArrayOf(0, 1, 0, 1, 1, 0, 0)
    private val interactionData = arrayOf(
        Pair(30, 400),
        Pair(397, 5),
        Pair(660, 400),
        Pair(890, 790),
        Pair(1240, 530),
        Pair(1607, 600)
    )
    private var storyArgument = arrayOf(
        R.raw.story_01_01_video,
        interactionData,
        R.raw.story_01_03_video
    )
    lateinit var currentArgument: Any
    lateinit var currentFragment: Any
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        val story = intent.getStringExtra("Story")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)
        println(story)
        currentFragment = getFragment(storyFragment[count])
        currentArgument = storyArgument[count]
        showFragment(currentFragment as Fragment)
        hideSystemUI()
        supportActionBar?.hide()
    }

    // reload fragment when resume
    override fun onResume() {
        super.onResume()
        println("resume fragment")
        showFragment(currentFragment as Fragment)
        supportActionBar?.hide()
        hideSystemUI()
    }

    // Display new fragment
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.story_host_fragment, fragment)
            addToBackStack(null)
        }.commit()
    }

    // convert data to fragment
    private fun getFragment(idFragment: Int): Fragment {
        return when (idFragment) {
            1 -> InteractButtonFragment()
            else -> VideoFragment()
        }
    }

    // When and Video or interaction is finished
    fun endEvent() {
        print("end")
        count++
        if (count == storyArgument.size) {
            endStory()
        } else {
            currentArgument = storyArgument[count]
            currentFragment = getFragment(storyFragment[count])
            showFragment(currentFragment as Fragment)
        }
    }

    // End Story and return to main activity
    fun endStory() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("Story", "frog")
        startActivity(intent)
    }

    // convert value position in interaction for all screen
    fun convertValue(height: Int, value: Int): Int {
        var convert: Float = 1036 / height.toFloat()
        println("$convert , $value , $height")
        return (value * convert).toInt()
    }

    fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
            )
    }

    override fun onBackPressed() {
        // Put your own code here which you want to run on back button click.
        endStory()
        super.onBackPressed()
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
    }
}
