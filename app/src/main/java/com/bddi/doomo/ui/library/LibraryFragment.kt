package com.bddi.doomo.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.bddi.doomo.model.Story
import com.bddi.doomo.model.User
import com.bddi.doomo.ui.account.AccountViewModel
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class LibraryFragment : Fragment() {

    private lateinit var librairyViewModel: LibraryViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        librairyViewModel =
            ViewModelProvider(this).get(LibraryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_library, container, false)

        // Display data in recyclerView in fragment_library.xml
        val adapter = object : FirestoreRecyclerAdapter<Story, StoryViewHolder>(
            librairyViewModel.options.setLifecycleOwner(
                this
            ).build()
        ) {
            // Get view
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
                val view: View = LayoutInflater.from(this@LibraryFragment.context).inflate(
                    R.layout.story_list_item,
                    parent,
                    false
                )
                return StoryViewHolder(view)
            }

            // Display story content in story_list_item.xml
            override fun onBindViewHolder(holder: StoryViewHolder, position: Int, model: Story) {


                val tvTitle: TextView = holder.itemView.findViewById(R.id.storyTitleView)
                tvTitle.text = model.title

                val storyInformationButton: FloatingActionButton = holder.itemView.findViewById(R.id.story_information_button)

                val ivStoryImage: ImageView = holder.itemView.findViewById(R.id.storyImageView)
                val imgStory = model.thumbnail_img
                Glide.with(context!!).load(imgStory).into(ivStoryImage)

                val playButton: FloatingActionButton = holder.itemView.findViewById(R.id.story_play_button)
                // TODO set good link to story

                val cvStoryCard: CardView = holder.itemView.findViewById((R.id.story_card))
                val tvLock: TextView = holder.itemView.findViewById((R.id.lock_text))
                val ivLock: ImageView = holder.itemView.findViewById((R.id.lock_icon))
                println("ATCHOIN : ")
                println("story1 : ${MainActivity.story_1}")
                println("story2 : ${MainActivity.story_2}")
                if ((position == 0 && MainActivity.story_1) || (position == 1 && MainActivity.story_2)) {

                    playButton.setOnClickListener {
                        (activity as MainActivity).startStory("DZevLTdzAisZUcPX8tup")
                    }
                    storyInformationButton.setOnClickListener() {
                        redirectToStoryDetails(model)
                    }
                    ivStoryImage.setOnClickListener {
                        redirectToStoryDetails(model)
                    }
                    cvStoryCard.foreground = null
                    tvLock.isVisible  = false
                    ivLock.isVisible = false
                }
                else {
                    playButton.isEnabled = false
                    playButton.isClickable = false
                    storyInformationButton.isEnabled = false
                    storyInformationButton.isClickable = false
                    tvLock.isVisible  = true
                    ivLock.isVisible = true
                }
            }
        }

        val accountButton: ImageButton = root.findViewById(R.id.account_button)
        accountButton.setOnClickListener {
            findNavController().navigate(R.id.child_security)
            (activity as MainActivity).uncheckAllItems()
        }

        // Get recyclerView and show informations
        var storiesRecyclerView: RecyclerView = root.findViewById(R.id.storiesRecyclerView)
        storiesRecyclerView.adapter = adapter
        storiesRecyclerView.setNestedScrollingEnabled(false)

        return root
    }

    private fun redirectToStoryDetails(story: Story) {
        findNavController().navigate(R.id.action_global_navigation_story_details)
        (activity as MainActivity).uncheckAllItems()
        (activity as MainActivity).currentModel = story
    }
}
