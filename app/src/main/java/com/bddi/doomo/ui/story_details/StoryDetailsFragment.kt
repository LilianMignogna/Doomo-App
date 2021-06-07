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
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.bddi.doomo.model.Story
import com.bddi.doomo.activity.StoryActivity
import com.bddi.doomo.activity.WrittenStoryActivity

class StoryDetailsFragment : Fragment() {

    private lateinit var storyDetailsViewModel: StoryDetailsViewModel
    private lateinit var currentStory: Story

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentStory = (activity as MainActivity).currentModel
        storyDetailsViewModel =
            ViewModelProvider(this).get(StoryDetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_story_details, container, false)

        val tvTitle: TextView = root.findViewById(R.id.text_story_title)
        val tvSubtitle: TextView = root.findViewById(R.id.text_story_subtitle)
        val tvTitle2: TextView = root.findViewById(R.id.text_story_title_2)
        val tvSubtitle2: TextView = root.findViewById(R.id.text_story_subtitle_2)

        val tvDescription: TextView = root.findViewById(R.id.text_story_description)
        val tvFunfact: TextView = root.findViewById(R.id.fun_fact_text)
        val tvReappear: TextView = root.findViewById(R.id.reappear_info_text)

        storyDetailsViewModel.text.observe(viewLifecycleOwner, Observer {
            tvTitle.text = currentStory.title
            tvSubtitle.text = currentStory.species
            tvTitle2.text = currentStory.title
            tvSubtitle2.text = currentStory.species

            tvDescription.text = currentStory.description
            tvFunfact.text = currentStory.funfact
            tvReappear.text = currentStory.reappear
        })

        val lauchStoryButton: Button = root.findViewById(R.id.button_start_interaction)
        lauchStoryButton.setOnClickListener {
            val intent = Intent(activity, StoryActivity::class.java)
            intent.putExtra("Story", "frog")
            startActivity(intent)
        }

        val readStoryButton: Button = root.findViewById(R.id.button_start_reading)
        readStoryButton.setOnClickListener {
            (activity as MainActivity).playSound(R.raw.clic_btn)
            val intent = Intent(activity, WrittenStoryActivity::class.java)
            intent.putExtra("WrittenStory", currentStory.written_story)
            startActivity(intent)
        }
        return root
    }
}
