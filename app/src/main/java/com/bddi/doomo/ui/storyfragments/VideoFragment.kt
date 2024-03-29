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
    lateinit var root: View

    private lateinit var videos: Array<Int>
    private var count: Int = 0
    private var video: Int = 0
    private lateinit var videoView: VideoView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        var isButtonVisible = false
        val context = requireContext()

        root = inflater.inflate(R.layout.fragment_story_video, container, false)

        videoView = root.findViewById(R.id.video_view)
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)

        videos = (activity as? StoryActivity)?.currentArgument as Array<Int>
        video = videos[count]

        // get directory path
        val currentPackage = activity?.packageName

        // Put video path in the layout
        val videoPath = "android.resource://$currentPackage/$video"
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        // define correct width for the video
        var height = context?.resources?.displayMetrics?.heightPixels
        if (height == null) {
            height = 1080
        }

        val layoutVideoView = videoView.layoutParams as ConstraintLayout.LayoutParams
        val width = (activity as? StoryActivity)?.convertValue(height, 2560)

        if (width != null) {
            layoutVideoView.width = width
        }
        videoView.layoutParams = layoutVideoView

        videoView.start()

        val buttonLayout = root.findViewById<ConstraintLayout>(R.id.layout_button)
        val pauseButton = root.findViewById<ImageView>(R.id.pause_button)
        val stopButton = root.findViewById<ImageView>(R.id.stop_button)
        var skipButton = root.findViewById<ImageView>(R.id.skip_button)

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
            if (isButtonVisible) {
                timer.start()
                (activity as? StoryActivity)?.endStory()
            }
        }

        // display pause button
        videoView.setOnClickListener {
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

        // Skip Video
        skipButton.setOnClickListener {
            if (isButtonVisible) {
                videoView.pause()
                timer.start()
                nextVideo(currentPackage)
            }
        }

        // event when video is ended
        videoView.setOnCompletionListener {
            nextVideo(currentPackage)
            println("the video just ended")
        }

        return root
    }

    fun nextVideo(currentPackage: String?) {
        count++
        if (count >= videos.size) {
            (activity as? StoryActivity)?.endEvent()
        } else {
            video = videos[count]
            val videoPath = "android.resource://$currentPackage/$video"
            val uri = Uri.parse(videoPath)
            videoView.setVideoURI(uri)
            videoView.start()
        }
    }

    override fun onResume() {
        super.onResume()
        val backgroundImage = root.findViewById<VideoView>(R.id.video_view)
        backgroundImage.start()
    }
}
