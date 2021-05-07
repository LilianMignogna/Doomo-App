package com.bddi.doomo.ui.story_details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bddi.doomo.R
import com.bddi.doomo.activity.StoryActivity

class StoryDetailsFragment : Fragment() {

    private lateinit var storyDetailsViewModel: StoryDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        storyDetailsViewModel =
            ViewModelProvider(this).get(StoryDetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_story_details, container, false)
        val textView: TextView = root.findViewById(R.id.text_story_details)
        storyDetailsViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
                textView.text = it
            }
        )

        val button: Button = root.findViewById(R.id.button_change_activity)
        button.setOnClickListener {
            val intent = Intent(activity, StoryActivity::class.java)
            intent.putExtra("Story", "frog")
            startActivity(intent)
        }
        return root
    }
}
