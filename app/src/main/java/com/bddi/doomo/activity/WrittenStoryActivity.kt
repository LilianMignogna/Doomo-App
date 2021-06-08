package com.bddi.doomo.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bddi.doomo.MainActivity
import com.bddi.doomo.R
import com.bddi.doomo.model.Chapter
import com.bddi.doomo.model.WrittenStory
import com.bddi.doomo.viewmodel.WrittenStoryViewModel
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class WrittenStoryActivity : AppCompatActivity() {

    private lateinit var writtenStoryViewModel: WrittenStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        writtenStoryViewModel =
            ViewModelProvider(this).get(WrittenStoryViewModel::class.java)

        val writtenStoryId = intent.getStringExtra("WrittenStory")
        setContentView(R.layout.activity_written_story)


        writtenStoryViewModel.getWrittenStoryInfos(writtenStoryId!!, this)
        writtenStoryViewModel.getWrittenStoryChapters(writtenStoryId!!, this)

        supportActionBar?.hide()

        val returnButton: TextView = findViewById(R.id.return_to_story_details_text)

        returnButton.setOnClickListener() {
            endStory()
        }
    }

    // End Story and return to main activity
    fun endStory() {
        val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("Story", "frog")
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Put your own code here which you want to run on back button click.
        endStory()
    }

    /**
     * Display initial values
     */
    fun setWrittenStoryInfos(writtenStory: WrittenStory) {
        val storyTitle: TextView = findViewById(R.id.story_title_text)
        val storySubtitle: TextView = findViewById(R.id.story_subtitle_text)
        val storyContainer: TextView = findViewById(R.id.story_text)
        val backgroudImage: ImageView = findViewById(R.id.background_image)
        storyTitle.text = writtenStory.title
        storySubtitle.text = writtenStory.subtitle
        storyContainer.text = "${writtenStory.title} ${writtenStory.subtitle}"

        val ivBackground: ImageView = findViewById(R.id.background_image)
        val imgBackground = writtenStory.background_img
        Glide.with(this.application).load(imgBackground).into(ivBackground)
    }

    fun processChapters(chapters: MutableList<Chapter>) {
        chapters.sortBy { it.order }
        val actionButton: MaterialButton = findViewById(R.id.action_button)
        val backwardButton: ImageButton = findViewById(R.id.backward_button)
        val storyContainer: TextView = findViewById(R.id.story_text)
        val soundButton: ImageButton = findViewById(R.id.sound_button)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        var index = -1

        actionButton.setOnClickListener() {
            index++
            loadData(
                chapters,
                index,
                actionButton,
                backwardButton,
                storyContainer,
                soundButton,
                progressBar
            )
        }
        backwardButton.setOnClickListener() {
            index--
            loadData(
                chapters,
                index,
                actionButton,
                backwardButton,
                storyContainer,
                soundButton,
                progressBar
            )
        }

    }
    fun loadData(
        chapters: MutableList<Chapter>,
        index: Int,
        actionButton: MaterialButton,
        backwardButton: ImageButton,
        storyContainer: TextView,
        soundButton: ImageButton,
        progressBar: ProgressBar
    ){
        // Quit if story is over
        if (index == chapters.size)
            endStory()

        actionButton.text = "Suivant"

        if(index == chapters.size -1)
            actionButton.text = "Terminer L'histoire"

        storyContainer.text = chapters[index].text
        //Change image if a new one is available
        if(chapters[index].background_img != ""){
            val ivBackground: ImageView = findViewById(R.id.background_image)
            val imgBackground = chapters[index].background_img
            Glide.with(this.application).load(imgBackground).into(ivBackground)
        }
        soundButton.isVisible = false
        if(chapters[index].sound != ""){
            soundButton.isVisible = true
            //Get sound id with the file name
            soundButton.setSoundOnImageButtonClicked(resources.getIdentifier(chapters[index].sound, "raw", getPackageName()))
        }
        // Determines progression level in percentage
        progressBar.progress =
            (((index.toDouble() + 1) / chapters.size.toDouble()) * 100).toInt()
    }

    /**
     * Set click listener on button
     */
    fun ImageButton.setSoundOnImageButtonClicked(sound: Int){
        val mp: MediaPlayer = MediaPlayer.create(this@WrittenStoryActivity, sound)
        setOnClickListener() {
            mp.start()
            false
        }
    }
}
