package com.bddi.doomo.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.bddi.doomo.activity.StoryActivity
import com.bddi.doomo.model.Story
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // Display data in recyclerView in fragment_home.xml
        val adapter = object : FirestoreRecyclerAdapter<Story, HomeViewHolder>(
            homeViewModel.options.setLifecycleOwner(
                this
            ).build()
        ) {
            // Get view
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
                val view: View = LayoutInflater.from(this@HomeFragment.context).inflate(
                    R.layout.favorite_story_list_item,
                    parent,
                    false
                )
                view.layoutParams = ViewGroup.LayoutParams((parent.width * 0.9).toInt(), ViewGroup.LayoutParams.MATCH_PARENT)
                return HomeViewHolder(view)
            }

            // Display story content in favorite_story_list_item.xml
            override fun onBindViewHolder(holder: HomeViewHolder, position: Int, model: Story) {
                val tvTitle: TextView = holder.itemView.findViewById(R.id.text_story_title)
                val tvSubtitle: TextView = holder.itemView.findViewById(R.id.text_story_subtitle)
                tvTitle.text = model.title
                tvSubtitle.text = model.species
                val ivThumbnail: ImageView = holder.itemView.findViewById(R.id.storyImageView)
                val imgThumbnail = model.thumbnail_img
                Glide.with(requireActivity().application).load(imgThumbnail).into(ivThumbnail)

                val storyInformationCard: CardView = holder.itemView.findViewById(R.id.favorite_story_card_item)
                storyInformationCard.setOnClickListener() {
                    root.findNavController().navigate(R.id.action_global_navigation_story_details)
                    (activity as MainActivity).uncheckAllItems()
                    (activity as MainActivity).currentModel = model
                }
                val playButton: FloatingActionButton = holder.itemView.findViewById(R.id.story_play_button)
                // TODO set good link to story
                playButton.setOnClickListener {
                    (activity as MainActivity).startStory("wsE8dOKqILn69dUNRRYL")
                }
            }
        }
        val accountButton: ImageButton = root.findViewById(R.id.account_button)
        accountButton.setOnClickListener {
            findNavController().navigate(R.id.account)
            (activity as MainActivity).uncheckAllItems()
        }

        val monthStoryButton: MaterialButton = root.findViewById(R.id.button_month_story)
        monthStoryButton.setOnClickListener {
            (activity as MainActivity).startStory("wsE8dOKqILn69dUNRRYL")
            (activity as MainActivity).playSound(R.raw.clic_btn)
            val intent = Intent(activity, StoryActivity::class.java)
            intent.putExtra("Story", "frog")
            startActivity(intent)
        }

        // Get recyclerView and show informations
        var storiesRecyclerView: RecyclerView = root.findViewById(R.id.favorite_stories_recycler_view)
        storiesRecyclerView.adapter = adapter

        return root
    }
}
