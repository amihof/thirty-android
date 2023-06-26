package com.example.inlupp1_amidala.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.inlupp1_amidala.R

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
            var round = ""
            val resultTextView = TextView(this)
            when (index){
                0 -> round = "Low"
                1 -> round = "4"
                2 -> round = "5"
                3 -> round = "6"
                4 -> round = "7"
                5 -> round = "8"
                6 -> round = "9"
                7 -> round = "10"
                8 -> round = "11"
                9 -> round = "12"
                else -> round = "ERROR"
            }
            resultTextView.text = "Round $round: $result"
            roundResultsLayout.addView(resultTextView)
        }

        // Display the final score in a TextView
        scoreTextView = findViewById(R.id.score_text_view)
        scoreTextView.text = "Total Score: $finalScore"
    }
}