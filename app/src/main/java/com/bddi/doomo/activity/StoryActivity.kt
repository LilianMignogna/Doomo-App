package com.bddi.doomo.activity

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

class StoryActivity : AppCompatActivity() {

    var transitioning = false
    private val boucle0 = arrayOf(
        R.raw.boucle0,
        R.raw.boucle1,
        R.raw.boucle2,
        R.raw.boucle3,
        R.raw.boucle4,
        R.raw.boucle5
    )
    private val interactionButtonData = Pair(
        arrayOf(
            R.raw.interaction_nenuphare_0,
            R.raw.interaction_nenuphare_1,
            R.raw.interaction_nenuphare_2,
            R.raw.interaction_nenuphare_3,
            R.raw.interaction_nenuphare_4,
            R.raw.interaction_nenuphare_5
        ),
        arrayOf(
            Pair(397, 5),
            Pair(660, 400),
            Pair(890, 790),
            Pair(1240, 530),
            Pair(1607, 600),
            Pair(50000, 5030)
        )
    )
    private val boucle6 = arrayOf(
        R.raw.boucle6,
        R.raw.boucle7,
        R.raw.boucle8
    )
    private val interactionPushData = arrayOf(
        R.raw.interact_push_00,
        R.raw.interact_push_01,
        R.raw.interact_push_02,
        R.raw.interact_push_03,
    )
    private val boucle10 = arrayOf(
        R.raw.boucle10,
        R.raw.boucle11,
        R.raw.boucle12
    )
    private val interactionClicData = arrayOf(
        R.drawable.interact_clic_00,
        R.drawable.interact_clic_01,
        R.raw.locked,
        R.raw.unlocked
    )
    private val boucle13 = arrayOf(
        R.raw.boucle13,
        R.raw.boucle14,
        R.raw.boucle16,
        R.raw.boucle17
    )
    private val interactionDragDataBG = arrayOf(
        R.drawable.interact_drag_00,
        R.drawable.interact_drag_01,
        R.drawable.interact_drag_02,
        R.drawable.interact_drag_03,
        R.drawable.interact_drag_04
    )
    private val interactionDragDataCord = arrayOf(
        Pair(775, 0),
        Pair(1480, 485),
        Pair(215, 710),
        Pair(775, 0),
        Pair(50000, 5030)
    )
    private val interactionDragData = Pair(interactionDragDataBG, interactionDragDataCord)
    private var storyArgument = arrayOf(
        boucle0,
        interactionClicData,
        boucle6,
        interactionButtonData,
        boucle10,
        interactionDragData,
        boucle13,
        interactionPushData,
        arrayOf(R.raw.boucle20)
    )
    private var storyFragment = arrayOf(
        "Video",
        "InteractClic",
        "Video",
        "InteractButton",
        "Video",
        "InteractDrag",
        "Video",
        "InteractPush",
        "Video",
    )
    private lateinit var TransitionAppearAnimation: AnimationDrawable
    private lateinit var rocketAppearImage: ImageView
    private lateinit var rocketDisappearImage: ImageView
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
        print("end $transitioning")
        if (transitioning === false) {
            transitioning = true
            count++
            if (count == storyArgument.size) {
                endStory()
            } else {
                if (storyFragment[count - 1] != storyFragment[count]) {
                    rocketAppearImage = findViewById<ImageView>(R.id.transition).apply {
                        setBackgroundResource(R.drawable.transition_appear)
                        TransitionAppearAnimation = background as AnimationDrawable
                    }
                    TransitionAppearAnimation.start()
                    TransitionAppearAnimation.onAnimationFinished {
                        currentArgument = storyArgument[count]
                        currentFragment = getFragment(storyFragment[count])
                        showFragment(currentFragment as Fragment)
                        rocketDisappearImage = findViewById<ImageView>(R.id.transition).apply {
                            setBackgroundResource(R.drawable.transition_disappear)
                            TransitionAppearAnimation = background as AnimationDrawable
                        }
                        TransitionAppearAnimation.start()
                        transitioning = false
                    }
                } else {
                    currentArgument = storyArgument[count]
                    currentFragment = getFragment(storyFragment[count])
                    showFragment(currentFragment as Fragment)
                    transitioning = false
                }
            }
        }
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
}
