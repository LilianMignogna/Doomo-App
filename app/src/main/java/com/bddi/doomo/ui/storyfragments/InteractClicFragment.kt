package com.bddi.doomo.ui.storyfragments

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bddi.doomo.R
import com.bddi.doomo.activity.StoryActivity

class InteractClicFragment : Fragment() {
    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        println("interaction fragment push loaded")

        root = inflater.inflate(R.layout.fragment_story_interact_clic, container, false)

        var argument = (activity as? StoryActivity)?.currentArgument as Array<Integer>

        (activity as? StoryActivity)?.transitioning = false

        (activity as? StoryActivity)?.playSound(R.raw.int1_riri)

        val currentPackage = activity?.packageName
        var imageFind = root.findViewById<ImageView>(R.id.image_view)
        val button_wrong_1 = root.findViewById<Button>(R.id.button_wrong_1)
        val button_wrong_2 = root.findViewById<Button>(R.id.button_wrong_2)
        val button_good_1 = root.findViewById<Button>(R.id.button_good_1)
        val stopButton = root.findViewById<ImageView>(R.id.stop_button)

        var transitionning = false

        var image = argument[0]
        var imagePath = "android.resource://$currentPackage/$image"
        var uriImage = Uri.parse(imagePath)
        imageFind.setImageURI(uriImage)

        val timer = object : CountDownTimer(1500, 10000) {
            override fun onTick(millisUntilFinished: Long) {
                println(millisUntilFinished)
            }

            override fun onFinish() {
                (activity as? StoryActivity)?.endEvent()
            }
        }

        // Leave Story
        // stopButton.setOnClickListener {
        //    (activity as? StoryActivity)?.endStory()
        // }

        button_wrong_1.setOnClickListener {
            var sound = argument[2]
            var soundPath = "android.resource://$currentPackage/$sound"
            var uriSound = Uri.parse(soundPath)
            playSound(uriSound)
        }

        button_wrong_2.setOnClickListener {
            var sound = argument[2]
            var soundPath = "android.resource://$currentPackage/$sound"
            var uriSound = Uri.parse(soundPath)
            playSound(uriSound)
        }

        button_good_1.setOnClickListener {
            var image = argument[1]
            var imagePath = "android.resource://$currentPackage/$image"
            var uriImage = Uri.parse(imagePath)
            imageFind.setImageURI(uriImage)

            var sound = argument[3]
            var soundPath = "android.resource://$currentPackage/$sound"
            var uriSound = Uri.parse(soundPath)
            playSound(uriSound)
            if (!transitionning) {
                transitionning = true
                timer.start()
            }
        }

        // Leave Story
        stopButton.setOnClickListener {
            (activity as? StoryActivity)?.endStory()
        }

        return root
    }

    private fun playSound(uriSound: Uri) {
        val mp: MediaPlayer = MediaPlayer.create(this.context, uriSound)
        mp.start()
    }

    private fun onFinish() {
        (activity as? StoryActivity)?.endEvent()
        println("the interaction just ended")
    }
}
