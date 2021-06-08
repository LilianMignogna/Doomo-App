package com.bddi.doomo.ui.storyfragments

import android.net.Uri
import android.os.Bundle
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

        var argument = (activity as? StoryActivity)?.currentArgument as Array<Pair<Int, Int>>

        val currentPackage = activity?.packageName
        val backgroundImage = root.findViewById<VideoView>(R.id.video_background)
        val video = R.raw.story_01_02_background
        val videoPath = "android.resource://$currentPackage/$video"
        val uri = Uri.parse(videoPath)
        var count = 1

        backgroundImage.setVideoURI(uri)

        backgroundImage.start()

        backgroundImage.setOnCompletionListener {
            backgroundImage.start()
        }

        val stopButton = root.findViewById<ImageView>(R.id.stop_button)

        var buttonImage = root.findViewById<ImageView>(R.id.interact_button)
        buttonImage.setImageResource(R.drawable.ripple)
        buttonImage.contentDescription = "button"

        var animalImage = root.findViewById<ImageView>(R.id.animal_img)
        animalImage.setImageResource(R.drawable.frog)
        animalImage.contentDescription = "the cute frog"

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
        val buttonImageX = (activity as? StoryActivity)?.convertValue(height, argument[count].first)
        val buttonImageY = (activity as? StoryActivity)?.convertValue(
            height,
            argument[count].second
        )

        // init animal position
        val layoutParamsAnimal = animalImage.layoutParams as ConstraintLayout.LayoutParams
        val animalImageX = (activity as? StoryActivity)?.convertValue(
            height,
            argument[count - 1].first
        )
        val animalImageY = (activity as? StoryActivity)?.convertValue(
            height,
            argument[count - 1].second
        )

        if (buttonImageX != null) {
            if (buttonImageY != null) {
                layoutParamsInteractButton.setMargins(buttonImageX, 0, 0, buttonImageY)
            }
        }
        buttonImage.layoutParams = layoutParamsInteractButton
        if (animalImageX != null) {
            if (animalImageY != null) {
                layoutParamsAnimal.setMargins(animalImageX, 0, 0, animalImageY)
            }
        }
        animalImage.layoutParams = layoutParamsAnimal

        // change button position to next one
        buttonImage.setOnClickListener {
            count++

            val animalImageX = (activity as? StoryActivity)?.convertValue(
                height,
                argument[count - 1].first
            )
            val animalImageY = (activity as? StoryActivity)?.convertValue(
                height,
                argument[count - 1].second
            )
            if (animalImageX != null) {
                if (animalImageY != null) {
                    layoutParamsAnimal.setMargins(animalImageX, 0, 0, animalImageY)
                }
            }
            animalImage.layoutParams = layoutParamsAnimal

            if (count >= argument.size) {
                onFinish()
            } else {
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

    private fun onFinish() {
        (activity as? StoryActivity)?.endEvent()
        println("the interaction just ended")
    }

    override fun onResume() {
        super.onResume()
        val backgroundImage = root.findViewById<VideoView>(R.id.video_background)
        backgroundImage.start()
    }
}
