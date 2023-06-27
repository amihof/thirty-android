package com.example.inlupp1_amidala.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
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

        // Retrieve the final score from the intent
        val finalScore = intent.getIntExtra("FINAL_SCORE", 0)
        val roundResults = intent.getIntArrayExtra("ROUND_RESULTS")

        val roundResultsLayout = findViewById<LinearLayout>(R.id.round_results_layout)
        roundResults?.forEachIndexed { index, result ->
            val round: String = when (index){
                0 -> "Low"
                in 1..9 -> (index + 3).toString()
                else -> "ERROR"
            }
            val resultTextView = TextView(this)
            resultTextView.text = "Round $round: $result"
            roundResultsLayout.addView(resultTextView)
        }

        // Display the final score in a TextView
        scoreTextView = findViewById(R.id.score_text_view)
        scoreTextView.text = "Total Score: $finalScore"
    }
}