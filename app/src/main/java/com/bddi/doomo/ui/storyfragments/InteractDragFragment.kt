package com.bddi.doomo.ui.storyfragments

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bddi.doomo.R
import com.bddi.doomo.activity.StoryActivity

class InteractDragFragment : Fragment() {
    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        println("interaction fragment loaded")

        root = inflater.inflate(R.layout.fragment_story_interact_drag, container, false)

        var currentArgument = (activity as? StoryActivity)?.currentArgument as Pair<Array<Int>, Array<Pair<Int, Int>>>
        var argument = currentArgument.second
        var argumentBG = currentArgument.first

        val currentPackage = activity?.packageName
        val backgroundImage = root.findViewById<ImageView>(R.id.image_background)
        var count = 0
        var image = argumentBG[count]
        var imagePath = "android.resource://$currentPackage/$image"
        var uriImage = Uri.parse(imagePath)
        backgroundImage.setImageURI(uriImage)

        val stopButton = root.findViewById<ImageView>(R.id.stop_button)

        var buttonImage = root.findViewById<ImageView>(R.id.interact_button)
        buttonImage.setImageResource(R.drawable.ripple)
        buttonImage.contentDescription = "button"

        var height = context?.resources?.displayMetrics?.heightPixels
        if (height == null) {
            height = 1080
        }

        val timer = object : CountDownTimer(1500, 10000) {
            override fun onTick(millisUntilFinished: Long) {
                println(millisUntilFinished)
            }

            override fun onFinish() {
                (activity as? StoryActivity)?.endEvent()
            }
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
        val buttonImageX = (activity as? StoryActivity)?.convertValue(height, argument[count].first)
        val buttonImageY = (activity as? StoryActivity)?.convertValue(
            height,
            argument[count].second
        )

        if (buttonImageX != null) {
            if (buttonImageY != null) {
                layoutParamsInteractButton.setMargins(buttonImageX, 0, 0, buttonImageY)
            }
        }
        buttonImage.layoutParams = layoutParamsInteractButton

        // change button position to next one
        buttonImage.setOnClickListener {
            count++

            if (count >= argument.size - 1 || count >= argumentBG.size - 1) {
                timer.start()
            }
            image = argumentBG[count]
            imagePath = "android.resource://$currentPackage/$image"
            uriImage = Uri.parse(imagePath)
            backgroundImage.setImageURI(uriImage)
            val buttonImageX = (activity as? StoryActivity)?.convertValue(
                height,
                argument[count].first
            )
            val buttonImageY = (activity as? StoryActivity)?.convertValue(
                height,
                argument[count].second
            )
            if (buttonImageX != null) {
                if (buttonImageY != null) {
                    layoutParamsInteractButton.setMargins(buttonImageX, 0, 0, buttonImageY)
                }
            }
            buttonImage.layoutParams = layoutParamsInteractButton
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
}
