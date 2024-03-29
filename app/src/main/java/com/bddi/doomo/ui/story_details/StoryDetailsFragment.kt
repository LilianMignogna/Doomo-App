package com.bddi.doomo.ui.story_details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.bddi.doomo.activity.WrittenStoryActivity
import com.bddi.doomo.model.Story
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class StoryDetailsFragment : Fragment() {

    private lateinit var storyDetailsViewModel: StoryDetailsViewModel
    private lateinit var currentStory: Story

    private lateinit var auth: FirebaseAuth

    // Storage connexion : get images
    val storage = Firebase.storage
    val storageRef = storage.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentStory = (activity as MainActivity).currentModel
        storyDetailsViewModel =
            ViewModelProvider(this).get(StoryDetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_story_details, container, false)

        auth = Firebase.auth
        val user = auth.currentUser

        // TextView From firebase Database
        val tvTitle: TextView = root.findViewById(R.id.text_story_title)
        val tvSubtitle: TextView = root.findViewById(R.id.text_story_subtitle)
        val tvTitle2: TextView = root.findViewById(R.id.text_story_title_2)
        val tvSubtitle2: TextView = root.findViewById(R.id.text_story_subtitle_2)
        val favButton: ImageButton = root.findViewById(R.id.button_favorite)

        val tvDescription: TextView = root.findViewById(R.id.text_story_description)
        val tvFunfact: TextView = root.findViewById(R.id.fun_fact_text)
        val tvReappear: TextView = root.findViewById(R.id.reappear_info_text)
        favButton.setOnClickListener(){
            storyDetailsViewModel.setFavorite("fav_story_1", user!!.uid, !MainActivity.fav_story_1)
            MainActivity.fav_story_1 = !MainActivity.fav_story_1
            println(MainActivity.fav_story_1)
            if(MainActivity.fav_story_1)
                favButton.setImageResource(R.drawable.ic_baseline_favorite_24)
            else
                favButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
        if(MainActivity.fav_story_1){
            favButton.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
        else{
            favButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }


        storyDetailsViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
                tvTitle.text = currentStory.title
                tvSubtitle.text = currentStory.species
                tvTitle2.text = currentStory.title
                tvSubtitle2.text = currentStory.species

                tvDescription.text = currentStory.description
                tvFunfact.text = currentStory.funfact
                tvReappear.text = currentStory.reappear
            }
        )

        storyDetailsViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
                tvTitle.text = currentStory.title
                tvSubtitle.text = currentStory.species
                tvTitle2.text = currentStory.title
                tvSubtitle2.text = currentStory.species

                resetStory()

                // ImageViews form Firebase Storage
                val ivHeader: ImageView = root.findViewById(R.id.StoryHeader)
                val imgHeader = currentStory.story_img
                Glide.with(requireActivity().application).load(imgHeader).into(ivHeader)
                val ivHome: ImageView = root.findViewById(R.id.map_image)
                val imgHome = currentStory.home_img
                Glide.with(requireActivity().application).load(imgHome).into(ivHome)

                val ivSpecies: ImageView = root.findViewById(R.id.animal_picture)
                val imgSpecies = currentStory.species_img
                Glide.with(requireActivity().application).load(imgSpecies).into(ivSpecies)

                if(currentStory.title != "Freddy le Gypaète"){
                    val lauchStoryButton: Button = root.findViewById(R.id.button_start_interaction)
                    lauchStoryButton.setOnClickListener {
                        (activity as MainActivity).startStory(currentStory.id)
                    }

                    val readStoryButton: Button = root.findViewById(R.id.button_start_reading)
                    readStoryButton.setOnClickListener {
                        (activity as MainActivity).playSound(R.raw.clic_btn)
                        val intent = Intent(activity, WrittenStoryActivity::class.java)
                        intent.putExtra("WrittenStory", currentStory.written_story)
                        startActivity(intent)
                    }
                }
            }
        )
        return root
    }

    private fun resetStory() {
        (activity as MainActivity).saveStory(" ")
    }
}
