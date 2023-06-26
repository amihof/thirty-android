package com.example.inlupp1_amidala.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.inlupp1_amidala.ViewModel.MyViewModel
import com.example.inlupp1_amidala.Model.DieColor
import com.example.inlupp1_amidala.Model.PointSpinner
import com.example.inlupp1_amidala.R

/**
 * Author: Amidala Hoffmén
 *
 * The main activity
 */
class MainActivity : AppCompatActivity() {

    //ArrayList that holds the ImageView of the dice
    private val diceImageViews = ArrayList<ImageView>()

    //ArrayList that holds the ImageView of the invis dice
    private val invisDiceImageViews = ArrayList<ImageView>()

    //Text view that shows which round and throw the user is on
    private lateinit var onWhichThrow: TextView
    private lateinit var onWhichRound: TextView

    //the button the user presses to roll the dice
    private lateinit var rollButton: Button

    //the button the user presses to submit their dice
    private lateinit var submitButton: Button

    //The textview that shows the Chosen Dice text
    private lateinit var chosenDice: TextView

    //text and button
    private lateinit var doneText: TextView
    private lateinit var doneButton: Button

    private lateinit var viewModel: MyViewModel

    //the drop-down menu (spinner)
    private lateinit var diceOptionsSpinner: Spinner

    //list of choices
    private val choices = listOf("Choose Value","Low", "4", "5", "6", "7", "8", "9", "10", "11", "12")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        //initialize views
        doneText = findViewById(R.id.done_text)
        doneButton = findViewById(R.id.done_button)
        rollButton = findViewById(R.id.button)
        submitButton = findViewById(R.id.button2)
        chosenDice = findViewById(R.id.textView)

        if(viewModel.dice.isEmpty()){
            //initialize the dice and add dice to the arraylist
            viewModel.initializeDice()
        }

        // Initialize the image views and add them to the arraylist
        initializeImageViews()

        // Initialize the point tracker
        viewModel.initializePointTracker()

        //initialize the spinner
        initializeSpinner()

        // Update the display of current throw and round
        updateThrowAndRound()

        // Update the dice images based on their states
        updateDiceImages()
        updateInvisDiceImages()

        if (viewModel.throwCounter == 2 or 3){
            choosePoint()
        }
    }

    // Update the display of current throw and round
    private fun updateThrowAndRound() {
        onWhichThrow = findViewById(R.id.which_throw)
        onWhichRound = findViewById(R.id.which_round)

        var throwMessage = ""
        if(viewModel.throwCounter == 3){
            throwMessage = "Throw ${3} of 3"
        }else{
            throwMessage = "Throw ${viewModel.throwCounter+1} of 3"
        }
        onWhichThrow.text = throwMessage

        val roundMessage = "Round ${viewModel.roundCounter+1} of 10"
        onWhichRound.text = roundMessage
    }

    // Initialize the spinner and set its item selection listener
    private fun initializeSpinner() {
        diceOptionsSpinner = findViewById(R.id.spinner)
        diceOptionsSpinner.setSelection(viewModel.selectedPosition)
        diceOptionsSpinner.isEnabled = viewModel.diceSpinnerEnabled

        diceOptionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get the selected position from the spinner
                viewModel.selectedPosition = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}//do nothing
        }

        viewModel.adapter = PointSpinner(this, R.layout.spinner_text_custom, choices, viewModel)
        viewModel.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        diceOptionsSpinner.adapter = viewModel.adapter
    }

    // Initialize the image views and add them to the arraylist
    private fun initializeImageViews() {
        diceImageViews.add(findViewById(R.id.dice1ImageView))
        diceImageViews.add(findViewById(R.id.dice2ImageView))
        diceImageViews.add(findViewById(R.id.dice3ImageView))
        diceImageViews.add(findViewById(R.id.dice4ImageView))
        diceImageViews.add(findViewById(R.id.dice5ImageView))
        diceImageViews.add(findViewById(R.id.dice6ImageView))

        invisDiceImageViews.add(findViewById(R.id.imageView))
        invisDiceImageViews.add(findViewById(R.id.imageView2))
        invisDiceImageViews.add(findViewById(R.id.imageView3))
        invisDiceImageViews.add(findViewById(R.id.imageView4))
        invisDiceImageViews.add(findViewById(R.id.imageView5))
        invisDiceImageViews.add(findViewById(R.id.imageView6))
    }

    // Update the visible dice images based on the dice states
    private fun updateDiceImages() {
        viewModel.dice.forEachIndexed { index, die ->
            val imageView = diceImageViews[index]
            val drawableId = when (die.color){
                DieColor.RED -> resources.getIdentifier("red${die.value}", "drawable", packageName)
                DieColor.GREY -> resources.getIdentifier("grey${die.value}", "drawable", packageName)
                else -> resources.getIdentifier("white${die.value}", "drawable", packageName)
            }
            imageView.setOnClickListener {
                if (viewModel.throwCounter != 0) {
                    if (viewModel.throwCounter == 3) {
                        if (die.color == DieColor.WHITE){
                            viewModel.greyInvisDie.add(die)
                            die.setDiceColor(DieColor.GREY)
                        }else if(die.color == DieColor.GREY){
                            viewModel.greyInvisDie.remove(die)
                            die.setDiceColor(DieColor.WHITE)
                        }else if (die.color == DieColor.RED){
                            imageView.isClickable = false
                        }
                    }else {
                        die.toggleRedState()
                    }
                }
                updateDiceImages()
                updateInvisDiceImages()
            }
            imageView.setImageResource(drawableId)
        }
    }

    // Update the invisible dice images based on the dice states
    private fun updateInvisDiceImages(){
        for (item in invisDiceImageViews){
            item.visibility = View.GONE
        }
        viewModel.greyInvisDie.forEachIndexed { index, die ->
            val imageView = invisDiceImageViews[index]
            imageView.visibility = View.GONE
            val drawableId = resources.getIdentifier("grey${die.value}", "drawable", packageName)
            imageView.setImageResource(drawableId)

            imageView.visibility = View.VISIBLE
            chosenDice.visibility = View.VISIBLE
            submitButton.visibility = View.VISIBLE
            imageView.setImageResource(drawableId)

            imageView.setOnClickListener {
                val correspondingDiceInDiceList = viewModel.dice.find { it.id == die.id }
                correspondingDiceInDiceList?.toggleGreyState()
                viewModel.greyInvisDie.remove(die)
                imageView.visibility = View.GONE
                updateDiceImages()
                updateInvisDiceImages()
            }
        }
    }


    // Roll the dice and update the images
    fun rollDice(src: View?) {
        if (viewModel.throwCounter == 2){
            // Roll the dice and reset their states
            viewModel.dice.forEach { die ->
                die.roll()
                die.resetDie()
            }
            updateDiceImages()
            updateInvisDiceImages()

            viewModel.throwCounter++
            choosePoint()
        }else if(viewModel.throwCounter < 2){
            // Roll the dice and reset their states
            viewModel.dice.forEach { die ->
                die.roll()
                die.resetDie()
            }
            updateDiceImages()
            updateInvisDiceImages()
            viewModel.throwCounter++
        }

        updateThrowAndRound()
    }

    // Handle the submission of chosen dice and check if the selected value is valid
    fun submitClicked(view: View) {
        // Get the selected value from the spinner
        val spinnerSelectedValue = diceOptionsSpinner.selectedItem.toString()

        if (viewModel.pointTracker.calculate(spinnerSelectedValue, viewModel.greyInvisDie)){
            // If the selected value is valid, clear the grey invisible dice and update the visible dice images
            viewModel.greyInvisDie.clear()
            viewModel.dice.forEachIndexed { index, die ->
                val imageView = diceImageViews[index]
                if (die.color == DieColor.GREY) {
                    // Change the image to red and toggle the red state of the die
                    val drawableId = resources.getIdentifier("red${die.value}", "drawable", packageName)
                    imageView.setImageResource(drawableId)
                    die.setDiceColor(DieColor.RED)
                    imageView.isClickable = false
                }
            }
            viewModel.diceSpinnerEnabled = false
            diceOptionsSpinner.isEnabled = false

            updateInvisDiceImages()
        }else{
            // If the selected value is not valid, show an error toast message
            val messageResId = when (viewModel.selectedPosition) {
                0 -> R.string.no_value_toast
                1 -> R.string.only_less_than_three_toast
                else -> R.string.wrong_math_toast
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        }
    }

    private fun choosePoint() {
        doneText.visibility = View.VISIBLE
        doneButton.visibility = View.VISIBLE
        rollButton.visibility = View.GONE

        doneButton.setOnClickListener{
            if (viewModel.roundCounter == 9){
                displayFinalScore();
            }else {
                if (viewModel.selectedPosition != 0) {
                    val view = diceOptionsSpinner.selectedView
                    view.isEnabled = false

                    val selectedPosition = diceOptionsSpinner.selectedItemPosition
                    if (selectedPosition != 0) {
                        viewModel.disableItem(selectedPosition)
                    }

                    diceOptionsSpinner.setSelection(0)
                    diceOptionsSpinner.isEnabled = true
                    viewModel.diceSpinnerEnabled = true

                    doneText.visibility = View.GONE
                    doneButton.visibility = View.GONE
                    rollButton.visibility = View.VISIBLE
                    chosenDice.visibility = View.GONE
                    submitButton.visibility = View.GONE
                    viewModel.throwCounter = 0
                    viewModel.roundCounter++
                    startNewRound()
                } else {
                    Toast.makeText(
                        this,
                        R.string.no_value_toast,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun startNewRound() {
        viewModel.greyInvisDie.clear()
        updateThrowAndRound()
        viewModel.dice.forEach { die ->
            die.resetDie()
            die.setDefaultValue()
        }
        updateDiceImages()
        updateInvisDiceImages()
    }

    private fun displayFinalScore() {
        val totalScore = viewModel.pointTracker.getTotalScore()
        val roundResults = viewModel.pointTracker.getRoundResults()

        val bundle = Bundle()
        bundle.putInt("FINAL_SCORE", totalScore as Int)

        // Create an intent and set the Bundle as its extra
        val intent = Intent(this, FinalScoreActivity::class.java)
        intent.putExtras(bundle)
        intent.putExtra("ROUND_RESULTS", roundResults)

        startActivity(intent)
    }

}


