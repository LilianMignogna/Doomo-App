package com.bddi.doomo.ui.storyfragments

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
        var count = false
        val context = requireContext()

        val root = inflater.inflate(R.layout.fragment_story_video, container, false)

        val videoView = root.findViewById<VideoView>(R.id.video_view)
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)

        val the_package = getActivity()?.getPackageName()

        val videoPath = "android.resource://$the_package/${R.raw.tetris}"
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        videoView.start()

        val pauseButton = root.findViewById<ImageView>(R.id.pause_button)

        val disappear = AnimationUtils.loadAnimation(context, R.anim.disappear)
        val appear = AnimationUtils.loadAnimation(context, R.anim.appear)

        val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                println(millisUntilFinished)
            }

            override fun onFinish() {
                pauseButton.startAnimation(disappear)
                count = false
                pauseButton.visibility = View.INVISIBLE
            }
        }

        pauseButton.setOnClickListener {
            if (count) {
                timer.start()
                if (videoView.isPlaying) {
                    videoView.pause()
                    (activity as? StoryActivity)?.hideSystemUI()
                    pauseButton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
                } else {
                    videoView.start()
                    (activity as? StoryActivity)?.hideSystemUI()
                    pauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                }
            }
        }

        videoView.setOnClickListener {
            timer.start()
            if (!count) {
                pauseButton.visibility = View.VISIBLE
                pauseButton.startAnimation(appear)
                count = true
            } else {
                timer.cancel()
                pauseButton.visibility = View.INVISIBLE
                pauseButton.startAnimation(disappear)
                count = false
            }
        }

        videoView.setOnCompletionListener {
            videoView.start()
            println("the video just endedt")
        }

        return root
    }
}
