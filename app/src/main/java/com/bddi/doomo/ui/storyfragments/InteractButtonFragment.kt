package com.bddi.doomo.ui.storyfragments

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bddi.doomo.R
import com.bddi.doomo.activity.StoryActivity

class InteractButtonFragment : Fragment() {
    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        println("interaction fragment loaded")

        root = inflater.inflate(R.layout.fragment_story_interact_button, container, false)
        (activity as? StoryActivity)?.playSound(R.raw.int2_riri)

        var argument = (activity as? StoryActivity)?.currentArgument as Pair<Array<Int>, Array<Pair<Int, Int>>>
        var argumentBG = argument.first
        var argumentCord = argument.second
        var animationOff = true

        // timer of visibility of pause button
        val timerAnimation = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                println(millisUntilFinished)
            }

            override fun onFinish() {
                animationOff = true
            }
        }

        var count = 0
        val currentPackage = activity?.packageName
        val backgroundImage = root.findViewById<VideoView>(R.id.video_background)
        val video = argumentBG[count]
        val videoPath = "android.resource://$currentPackage/$video"
        val uri = Uri.parse(videoPath)
        backgroundImage.setVideoURI(uri)
        backgroundImage.start()

        val stopButton = root.findViewById<ImageView>(R.id.stop_button)

        var buttonImage = root.findViewById<ImageView>(R.id.interact_button)
        buttonImage.setImageResource(R.drawable.ripple)
        buttonImage.contentDescription = "button"

        var height = context?.resources?.displayMetrics?.heightPixels
        if (height == null) {
            height = 1080
        }

        // define correct width for the video
        val layoutVideoView = backgroundImage.layoutParams as ConstraintLayout.LayoutParams
        val width = (activity as? StoryActivity)?.convertValue(height, 2560)

        if (width != null) {
            layoutVideoView.width = width
        }
        backgroundImage.layoutParams = layoutVideoView

        // init button position
        val layoutParamsInteractButton = buttonImage.layoutParams as ConstraintLayout.LayoutParams
        val buttonImageX = (activity as? StoryActivity)?.convertValue(height, argumentCord[count].first)
        val buttonImageY = (activity as? StoryActivity)?.convertValue(
            height,
            argumentCord[count].second
        )

        if (buttonImageX != null) {
            if (buttonImageY != null) {
                layoutParamsInteractButton.setMargins(buttonImageX, 0, 0, buttonImageY)
            }
        }
        buttonImage.layoutParams = layoutParamsInteractButton

        val timer = object : CountDownTimer(1500, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                println("test")
            }

            override fun onFinish() {
                (activity as? StoryActivity)?.endEvent()
            }
        }

        // change button position to next one
        buttonImage.setOnClickListener {
            if (animationOff) {
                timerAnimation.start()
                animationOff = false
                count++
                if (count == argumentCord.size - 1) {
                    timer.start()
                }
                if (count < argumentCord.size) {
                    val buttonImageX = (activity as? StoryActivity)?.convertValue(
                        height,
                        argumentCord[count].first
                    )
                    val buttonImageY = (activity as? StoryActivity)?.convertValue(
                        height,
                        argumentCord[count].second
                    )
                    if (buttonImageX != null) {
                        if (buttonImageY != null) {
                            layoutParamsInteractButton.setMargins(buttonImageX, 0, 0, buttonImageY)
                        }
                    }
                    buttonImage.layoutParams = layoutParamsInteractButton

                    val video = argumentBG[count]
                    val videoPath = "android.resource://$currentPackage/$video"
                    val uri = Uri.parse(videoPath)
                    backgroundImage.setVideoURI(uri)
                    backgroundImage.start()
                }
            }
        }

        // Leave Story
        stopButton.setOnClickListener {
            (activity as? StoryActivity)?.endStory()
        }

        backgroundImage.setOnClickListener {
            (activity as? StoryActivity)?.hideSystemUI()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        val backgroundImage = root.findViewById<VideoView>(R.id.video_background)
        backgroundImage.start()
    }
}
