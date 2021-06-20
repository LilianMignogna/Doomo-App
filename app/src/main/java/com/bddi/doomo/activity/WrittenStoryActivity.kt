package com.bddi.doomo.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
        val introTitle: TextView = findViewById(R.id.intro_story_title)
        val introDetail: TextView = findViewById(R.id.intro_story_detail)

        val storyTitle: TextView = findViewById(R.id.story_title_text)
        val storySubtitle: TextView = findViewById(R.id.story_subtitle_text)
        val storyContainer: TextView = findViewById(R.id.story_text)
        storyTitle.text = writtenStory.title
        storySubtitle.text = writtenStory.subtitle

        introTitle.text = writtenStory.title
        introDetail.text = writtenStory.subtitle

        val ivBackground: ImageView = findViewById(R.id.background_image)
        val imgBackground = writtenStory.background_img
        Glide.with(this.application).load(imgBackground).into(ivBackground)

        val endingPicture: ImageView = findViewById(R.id.ending_picture)
        val endingImg = writtenStory.ending_img
        Glide.with(this.application).load(endingImg).into(endingPicture)
    }

    fun processChapters(chapters: MutableList<Chapter>) {
        chapters.sortBy { it.order }
        val actionButton: MaterialButton = findViewById(R.id.action_button)
        val backwardButton: ImageButton = findViewById(R.id.backward_button)
        val storyContainer: TextView = findViewById(R.id.story_text)
        val soundButton: ImageButton = findViewById(R.id.sound_button)
        val sound2Button: ImageButton = findViewById(R.id.sound2_button)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        var index = -1


        actionButton.setOnClickListener() {
            if(index < chapters.size-1){
                index++
                loadData(
                    chapters,
                    index,
                    actionButton,
                    backwardButton,
                    storyContainer,
                    soundButton,
                    sound2Button,
                    progressBar
                )
            }
            else{
                actionButton.text = "Retourner à la bibliothèque"
                // Quit if story is over
                actionButton.setOnClickListener() {
                    endStory()
                }
                showLast()
            }
        }
        backwardButton.setOnClickListener() {
            println(index)
            if(index > 0 && index < chapters.size-1){
                index--
                loadData(
                    chapters,
                    index,
                    actionButton,
                    backwardButton,
                    storyContainer,
                    soundButton,
                    sound2Button,
                    progressBar
                )
            }
        }
    }
    fun loadData(
        chapters: MutableList<Chapter>,
        index: Int,
        actionButton: MaterialButton,
        backwardButton: ImageButton,
        storyContainer: TextView,
        soundButton: ImageButton,
        sound2Button: ImageButton,
        progressBar: ProgressBar
    ){

        hideFirst()
        val ivBackground: ImageView = findViewById(R.id.background_image)
        val soundButtonName: TextView = findViewById(R.id.sound_button_name)
        val sound2ButtonName: TextView = findViewById(R.id.sound2_button_name)

        actionButton.text = "Suivant"

        storyContainer.text = chapters[index].text.replace("\\n", "\n")

        //Change image if a new one is available
        if(chapters[index].background_img != ""){
            val imgBackground = chapters[index].background_img
            Glide.with(this.application).load(imgBackground).into(ivBackground)
        }
        soundButton.isVisible = false
        soundButtonName.isVisible = false
        sound2Button.isVisible = false
        sound2ButtonName.isVisible = false
        if(chapters[index].sound != ""){
            soundButtonName.text = chapters[index].sound.capitalize()
            soundButton.isVisible = true
            soundButtonName.isVisible = true
            //Get sound id with the file name
            soundButton.setSoundOnImageButtonClicked(resources.getIdentifier(chapters[index].sound, "raw", getPackageName()))
        }
        if(chapters[index].sound2 != ""){
            sound2ButtonName.text = chapters[index].sound2.capitalize()
            sound2Button.isVisible = true
            sound2ButtonName.isVisible = true
            //Get sound id with the file name
            sound2Button.setSoundOnImageButtonClicked(resources.getIdentifier(chapters[index].sound2, "raw", getPackageName()))
        }
        // Determines progression level in percentage
        progressBar.progress =
            (((index.toDouble() + 1) / chapters.size.toDouble()) * 100).toInt()
    }

    fun showFirst(){
        val introLayout: ConstraintLayout = findViewById(R.id.intro_layout)
        val storyLayout: ConstraintLayout = findViewById(R.id.story_layout)
        introLayout.isVisible = true
        storyLayout.isVisible = false
    }
    fun hideFirst(){
        val introLayout: ConstraintLayout = findViewById(R.id.intro_layout)
        val storyLayout: ConstraintLayout = findViewById(R.id.story_layout)
        introLayout.isVisible = false
        storyLayout.isVisible = true
    }
    fun showLast(){
        val storyLayout: ConstraintLayout = findViewById(R.id.story_layout)
        val endLayout: ConstraintLayout = findViewById(R.id.end_layout)
        storyLayout.isVisible = false
        endLayout.isVisible = true
    }
    fun hideLast(){
        val storyLayout: ConstraintLayout = findViewById(R.id.story_layout)
        val endLayout: ConstraintLayout = findViewById(R.id.end_layout)
        storyLayout.isVisible = true
        endLayout.isVisible = false
    }
    /**
     * Set click listener on button and trigger sound
     */
    fun ImageButton.setSoundOnImageButtonClicked(sound: Int){
        val mp: MediaPlayer = MediaPlayer.create(this@WrittenStoryActivity, sound)
        setOnClickListener() {
            mp.start()
            false
        }
    }
}
