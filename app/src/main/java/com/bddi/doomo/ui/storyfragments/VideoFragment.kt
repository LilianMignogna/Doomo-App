package com.bddi.doomo.ui.story

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bddi.doomo.R
import com.bddi.doomo.activity.StoryActivity

class VideoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        var isButtonVisible = false
        val context = requireContext()

        val root = inflater.inflate(R.layout.fragment_story_video, container, false)

        val videoView = root.findViewById<VideoView>(R.id.video_view)
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)

        val video = (activity as? StoryActivity)?.currentArgument

        // get directory path
        val currentPackage = activity?.packageName

        // Put video path in the layout
        val videoPath = "android.resource://$currentPackage/$video"
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        videoView.start()

        val buttonLayout = root.findViewById<ConstraintLayout>(R.id.layout_button)
        val pauseButton = root.findViewById<ImageView>(R.id.pause_button)
        val stopButton = root.findViewById<ImageView>(R.id.stop_button)

        val disappear = AnimationUtils.loadAnimation(context, R.anim.disappear)
        val appear = AnimationUtils.loadAnimation(context, R.anim.appear)

        // timer of visibility of pause button
        val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                println(millisUntilFinished)
            }

            override fun onFinish() {
                buttonLayout.startAnimation(disappear)
                isButtonVisible = false
                buttonLayout.visibility = View.INVISIBLE
            }
        }

        // pause the video 
        pauseButton.setOnClickListener {
            if (isButtonVisible) {
                timer.start()
                if (videoView.isPlaying) {
                    videoView.pause()
                    pauseButton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
                } else {
                    videoView.start()
                    pauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                }
            }
        }

        // Leave Story
        stopButton.setOnClickListener {
            (activity as? StoryActivity)?.endStory()
        }

        // display pause button
        videoView.setOnClickListener {
            // (activity as? StoryActivity)?.endEvent()
            println("touch")
            (activity as? StoryActivity)?.hideSystemUI()
            timer.start()
            if (!isButtonVisible) {
                buttonLayout.visibility = View.VISIBLE
                buttonLayout.alpha = 1F
                buttonLayout.startAnimation(appear)
                isButtonVisible = true
            } else {
                timer.cancel()
                buttonLayout.visibility = View.INVISIBLE
                buttonLayout.startAnimation(disappear)
                isButtonVisible = false
            }
        }

        // event when video is ended
        videoView.setOnCompletionListener {
            (activity as? StoryActivity)?.endEvent()
            println("the video just ended")
        }

        return root
    }
}
