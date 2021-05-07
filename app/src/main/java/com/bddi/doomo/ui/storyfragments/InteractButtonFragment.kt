package com.bddi.doomo.ui.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bddi.doomo.R
import com.bddi.doomo.activity.StoryActivity

class InteractButtonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        println("interaction fragment loaded")

        val root = inflater.inflate(R.layout.fragment_story_interact_button, container, false)
        val backgroundImage = root.findViewById<ImageView>(R.id.image_background)
        backgroundImage.setImageResource(R.drawable.illustration_sans_titre_33)
        backgroundImage.contentDescription = "background"

        var buttonImage = root.findViewById<ImageView>(R.id.interact_button)
        buttonImage.setImageResource(R.drawable.ripple)
        buttonImage.contentDescription = "button"

        var height = context?.resources?.displayMetrics?.heightPixels
        if (height == null) {
            height = 1036
        }

        // init button position
        val layoutParams = buttonImage.layoutParams as ConstraintLayout.LayoutParams
        val left = (activity as? StoryActivity)?.convertValue(height, 397)
        val bottom = (activity as? StoryActivity)?.convertValue(height, 5)

        println("height : $height, Left : $left, Bottom : $bottom")
        if (left != null) {
            if (bottom != null) {
                layoutParams.setMargins(left, 0, 0, bottom)
            }
        }
        buttonImage.layoutParams = layoutParams

        // change button position to next one
        buttonImage.setOnClickListener {
            val left = (activity as? StoryActivity)?.convertValue(height, 670)
            val bottom = (activity as? StoryActivity)?.convertValue(height, 400)
            if (left != null) {
                if (bottom != null) {
                    layoutParams.setMargins(left, 0, 0, bottom)
                }
            }
            buttonImage.layoutParams = layoutParams
        }

        backgroundImage.setOnClickListener {
            (activity as? StoryActivity)?.hideSystemUI()
        }

        return root
    }

    fun onFinish() {
        (activity as? StoryActivity)?.endEvent()
        println("the interaction just ended")
    }
}
