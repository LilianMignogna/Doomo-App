package com.bddi.doomo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R

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
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val storyDetailsButton: Button = root.findViewById(R.id.to_story_details_button)

        // Add link to the story_details fragment
        storyDetailsButton.setOnClickListener {
            root.findNavController().navigate(R.id.action_global_navigation_story_details)
            (activity as MainActivity).uncheckAllItems()
        }
        return root
    }
}