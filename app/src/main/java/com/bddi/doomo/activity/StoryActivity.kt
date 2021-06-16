package com.bddi.doomo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.bddi.doomo.ui.story.VideoFragment
import com.bddi.doomo.ui.storyfragments.InteractButtonFragment
import com.bddi.doomo.ui.storyfragments.InteractClicFragment
import com.bddi.doomo.ui.storyfragments.InteractDragFragment
import com.bddi.doomo.ui.storyfragments.InteractPushFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

data class fragmentStoryType(
    var type: String? = null,
    var sound: Int? = null,
    var argument: JsonObject? = null
)

class StoryActivity : AppCompatActivity() {

    var transitioning = false
    private lateinit var TransitionAppearAnimation: AnimationDrawable
    private lateinit var rocketAppearImage: ImageView
    private lateinit var rocketDisappearImage: ImageView
    lateinit var currentArgument: Any
    lateinit var currentFragment: Any
    var story: String = "none"
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        story = intent.getStringExtra("Story").toString()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)
        println(story)
//        currentFragment = getFragment(storyFragment[count])
//        currentArgument = storyArgument[count]

        val obj = Gson().toJson(getJsonDataFromAsset(this, "storyArgument.json"))
        println(obj)
        showFragment(currentFragment as Fragment)
        hideSystemUI()

        rocketAppearImage = findViewById<ImageView>(R.id.transition).apply {
            setBackgroundResource(R.drawable.transition_appear)
            TransitionAppearAnimation = background as AnimationDrawable
        }
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
    private fun getFragment(typeFragment: String): Fragment {
        return when (typeFragment) {
            "InteractButton" -> InteractButtonFragment()
            "InteractPush" -> InteractPushFragment()
            "InteractClic" -> InteractClicFragment()
            "InteractDrag" -> InteractDragFragment()
            else -> VideoFragment()
        }
    }

    // When and Video or interaction is finished
    fun endEvent() {
//        print("end $transitioning")
//        if (transitioning === false) {
//            transitioning = true
//            count++
//            if (count == storyArgument.size) {
//                endStory()
//            } else {
//                if (storyFragment[count - 1] != storyFragment[count]) {
//                    rocketAppearImage = findViewById<ImageView>(R.id.transition).apply {
//                        setBackgroundResource(R.drawable.transition_appear)
//                        TransitionAppearAnimation = background as AnimationDrawable
//                    }
//                    TransitionAppearAnimation.start()
//                    TransitionAppearAnimation.onAnimationFinished {
//                        currentArgument = storyArgument[count]
//                        currentFragment = getFragment(storyFragment[count])
//                        showFragment(currentFragment as Fragment)
//                        rocketDisappearImage = findViewById<ImageView>(R.id.transition).apply {
//                            setBackgroundResource(R.drawable.transition_disappear)
//                            TransitionAppearAnimation = background as AnimationDrawable
//                        }
//                        TransitionAppearAnimation.start()
//                        transitioning = false
//                    }
//                } else {
//                    currentArgument = storyArgument[count]
//                    currentFragment = getFragment(storyFragment[count])
//                    showFragment(currentFragment as Fragment)
//                    transitioning = false
//                }
//            }
//        }
    }

    // End Story and return to main activity
    fun endStory() {
        val intent = Intent(this, MainActivity::class.java)
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

    fun playSound(sound: Int) {
        val mp: MediaPlayer = MediaPlayer.create(this, sound)
        mp.start()
    }

    private fun AnimationDrawable.onAnimationFinished(block: () -> Unit) {
        var duration: Long = 0
        for (i in 0..numberOfFrames) {
            duration += getDuration(i)
        }
        Handler().postDelayed(
            {
                block()
            },
            duration
        )
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}
