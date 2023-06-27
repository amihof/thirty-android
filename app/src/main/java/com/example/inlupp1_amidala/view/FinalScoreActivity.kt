package com.example.inlupp1_amidala.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.inlupp1_amidala.R

/**
 * Author: Amidala Hoffmén
 *
 * Activity class that displays the final score and round results.
 */
class FinalScoreActivity : AppCompatActivity() {
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_score)

        //Retrieve the final score and array of round results from the intent
        val finalScore = intent.getIntExtra("FINAL_SCORE", 0)
        val roundResults = intent.getIntArrayExtra("ROUND_RESULTS")

        val roundResultsLayout = findViewById<LinearLayout>(R.id.roundResultsLayout)

        //Iterate over each round result and create a TextView to display it
        roundResults?.forEachIndexed { index, result ->
            val roundText = when (index) {
                0 -> "Low"
                in 1..9 -> (index + 3).toString()
                else -> "ERROR"
            }
            //Create a new TextView and set its text to the round result
            val resultTextView = TextView(this)
            resultTextView.text = getString(R.string.round_result_text_list, roundText, result)

            //Add the TextView to the LinearLayout
            roundResultsLayout.addView(resultTextView)
        }

        //Display the final score in a TextView
        scoreTextView = findViewById(R.id.scoreTextView)
        scoreTextView.text = getString(R.string.total_score_text, finalScore)

        //Initialize the play again button and set a click listener
        val playAgainButton = findViewById<Button>(R.id.playAgainButton)
        playAgainButton.setOnClickListener {
            //Start the game again by launching the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() //Finish the current activity to prevent going back to it with the back button
        }

    }
}