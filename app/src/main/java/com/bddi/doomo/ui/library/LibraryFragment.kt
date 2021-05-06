package com.bddi.doomo.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bddi.doomo.R
import com.bddi.doomo.model.Story
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StoryViewHolder (itemView: View): RecyclerView.ViewHolder(itemView)

class LibraryFragment : Fragment() {

    private lateinit var librairyViewModel: LibraryViewModel

    // Data Base Connection : get data
    val db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        librairyViewModel =
                ViewModelProvider(this).get(LibraryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_library, container, false)
        val textView: TextView = root.findViewById(R.id.text_library)
        librairyViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })


        val query = db.collection("Stories")
        val options = FirestoreRecyclerOptions.Builder<Story>().setQuery(query, Story::class.java)
            .setLifecycleOwner(this).build()
        val adapter = object: FirestoreRecyclerAdapter<Story, StoryViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
                val view: View = LayoutInflater.from(this@LibraryFragment.context ).inflate(R.layout.story_list_item, parent, false)
                return StoryViewHolder(view)
            }

            override fun onBindViewHolder(holder: StoryViewHolder, position: Int, model: Story) {
                val tvTitle: TextView = holder.itemView.findViewById(R.id.storyTitleView)
                //val tvDescription: TextView = holder.itemView.findViewById(R.id.storyDescriptionView)
                tvTitle.text = model.title
                //tvDescription.text = model.description
            }

        }

        var storiesRecyclerView: RecyclerView = root.findViewById(R.id.storiesRecyclerView)
        storiesRecyclerView.adapter = adapter
        //storiesRecyclerView.layoutManager = GridLayoutManager(this.context, 2)



        return root
    }
}