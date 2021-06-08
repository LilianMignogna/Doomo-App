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

class InteractPushFragment : Fragment() {
    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        println("interaction fragment push loaded")

        root = inflater.inflate(R.layout.fragment_story_interact_push, container, false)

        var argument = (activity as? StoryActivity)?.currentArgument as Array<Integer>

        val currentPackage = activity?.packageName
        val layout = root.findViewById<ConstraintLayout>(R.id.layout_button)
        val videoFind = root.findViewById<VideoView>(R.id.video_view)

        var clickPossible = true
        var count = 0

        var video = argument[1]
        var videoPath = "android.resource://$currentPackage/$video"
        var uriVideo = Uri.parse(videoPath)
        videoFind.setVideoURI(uriVideo)
        videoFind.start()

        videoFind.setOnCompletionListener {
            if (count >= argument.size - 1) {
                (activity as? StoryActivity)?.endEvent()
            } else if (count == 0) {
                videoFind.start()
            }
            count = 0
        }

        val timer = object : CountDownTimer(1000, 10000) {
            override fun onTick(millisUntilFinished: Long) {
                println(millisUntilFinished)
            }

            override fun onFinish() {
                clickPossible = true
            }
        }

        // Leave Story
        // stopButton.setOnClickListener {
        //    (activity as? StoryActivity)?.endStory()
        // }

        layout.setOnClickListener {
            (activity as? StoryActivity)?.hideSystemUI()
            println("CLICK CLICK CLICK")
            if (clickPossible && count != argument.size - 1) {
                println("CLICK CLICK CLICK CLICK")
                timer.start()
                clickPossible = false
                count++
                video = argument[count]
                videoPath = "android.resource://$currentPackage/$video"
                println(videoPath)
                uriVideo = Uri.parse(videoPath)
                videoFind.setVideoURI(uriVideo)
                videoFind.start()
            }
        }

        return root
    }

    private fun onFinish() {
        (activity as? StoryActivity)?.endEvent()
        println("the interaction just ended")
    }
}
