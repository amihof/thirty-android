package com.example.inlupp1_amidala.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.inlupp1_amidala.model.DieColor
import com.example.inlupp1_amidala.model.PointSpinnerAdapter
import com.example.inlupp1_amidala.R
import com.example.inlupp1_amidala.databinding.ActivityMainBinding
import com.example.inlupp1_amidala.viewmodel.MyViewModel

/**
 * Author: Amidala Hoffmén
 *
 * The main activity that manages the game and the user interface.
 * It handles dice rolling, displaying and updating UI elements, and managing user interactions.
 */
class MainActivity : AppCompatActivity() {

    //View binding for the activity
    private lateinit var binding: ActivityMainBinding

    //The view model that saves the data
    private lateinit var viewModel: MyViewModel

    //ArrayLists that holds the ImageView of the dice
    private val diceImageViews = ArrayList<ImageView>()
    private val selectedDiceImageViews = ArrayList<ImageView>()

    //The drop-down menu (spinner)
    private lateinit var diceOptionsSpinner: Spinner

    //Map of the dice images
    private val drawableDiceIds = mapOf(
        DieColor.RED to listOf(
            R.drawable.red1,
            R.drawable.red2,
            R.drawable.red3,
            R.drawable.red4,
            R.drawable.red5,
            R.drawable.red6
        ),
        DieColor.GREY to listOf(
            R.drawable.grey1,
            R.drawable.grey2,
            R.drawable.grey3,
            R.drawable.grey4,
            R.drawable.grey5,
            R.drawable.grey6
        ),
        DieColor.WHITE to listOf(
            R.drawable.white1,
            R.drawable.white2,
            R.drawable.white3,
            R.drawable.white4,
            R.drawable.white5,
            R.drawable.white6
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        //Check if the dice are already initialized, if not, initialize them
        if(viewModel.dice.isEmpty()){
            viewModel.initializeDice()
        }

        //Initialize the image views and add them to the arraylist
        initializeViews()

        //Initialize the point tracker
        viewModel.initializePointTracker()

        //Initialize the spinner
        initializeSpinner()

        initializeButtons()

        //Update the display of current throw and round
        updateThrowAndRound()

        //Update the dice images
        updateDiceImages()
        updateSelectedDiceImages()

        //Check if it's the end of a round and handle it
        if (viewModel.throwCounter == 2 or 3){
            endOfRound()
        }
    }

    /**
     * Initializes the buttons and sets click listeners to the buttons
     */
    private fun initializeButtons() {
        //set the ClickListener for the buttons
        binding.submitButton.setOnClickListener {
            submitClicked()
        }

        binding.rollButton.setOnClickListener {
            rollDice()
        }
    }

    /**
     * Initializes the image views and add them to Arraylists.
     */
    private fun initializeViews() {
        with(binding) {
            diceImageViews.addAll(
                listOf(
                    dice1ImageView,
                    dice2ImageView,
                    dice3ImageView,
                    dice4ImageView,
                    dice5ImageView,
                    dice6ImageView
                )
            )

            selectedDiceImageViews.addAll(
                listOf(
                    chosenDiceImageView,
                    chosenDiceImageView2,
                    chosenDiceImageView3,
                    chosenDiceImageView4,
                    chosenDiceImageView5,
                    chosenDiceImageView6
                )
            )
        }
    }

    /**
     * Updates the display of the current throw and round in the UI.
     */
    private fun updateThrowAndRound() {
        with(binding) {
            //Update the throw message based on the throw counter
            val throwMessage = "Throw ${if(viewModel.throwCounter == 3) 3 else viewModel.throwCounter+1} of 3"
            whichThrowTextView.text = throwMessage

            //Update the round message based on the round counter
            val roundMessage = "Round ${viewModel.roundCounter + 1} of 10"
            whichRoundTextView.text = roundMessage
        }
    }

    /**
     * Initializes the spinner and sets its item selection listener to handle user choices.
     */
    private fun initializeSpinner() {
        //Get the choices array from resources
        val choices = resources.getStringArray(R.array.choices_array).toList()

        //Initialize the spinner and set its properties
        diceOptionsSpinner = binding.spinner
        diceOptionsSpinner.setSelection(viewModel.selectedPosition)
        diceOptionsSpinner.isEnabled = viewModel.diceSpinnerEnabled

        //Set the item selection listener for the spinner
        diceOptionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Get the selected position from the spinner and update the view model
                viewModel.selectedPosition = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}//Do nothing
        }

        //Create the adapter for the spinner and set its properties
        viewModel.adapter = PointSpinnerAdapter(this, R.layout.spinner_text_custom, choices, viewModel)
        viewModel.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        diceOptionsSpinner.adapter = viewModel.adapter
    }

    /**
     * Updates the dice images based on the current state of the dice.
     */
    private fun updateDiceImages() {
        //Set the correct image for the dice based on the dice color
        viewModel.dice.forEachIndexed { index, die ->
            val imageView = diceImageViews[index]
            val drawableId = drawableDiceIds[die.color]?.get(die.value - 1) ?: throw IllegalArgumentException("Invalid die color or value")
            //Set the click listener for the image view
            imageView.setOnClickListener {
                //If throwCounter is 0 it means that the user has not threw the dice once
                if (viewModel.throwCounter != 0) {
                    //If throwCounter is 3 it means that the user has threw the dice 3 times that round and is at the end of the round
                    if (viewModel.throwCounter == 3) {
                        when (die.color) {
                            DieColor.WHITE -> {
                                viewModel.selectedDice.add(die)
                                die.setDiceColor(DieColor.GREY)
                            }
                            DieColor.GREY -> {
                                viewModel.selectedDice.remove(die)
                                die.setDiceColor(DieColor.WHITE)
                            }
                            DieColor.RED -> {
                                imageView.isClickable = false
                            }
                        }
                    }else { //If the throwCounter is not 3, it means that the user has not threw their dice 3 times yet
                        die.toggleRedState()
                    }
                }
                //Update the dice images
                updateDiceImages()
                updateSelectedDiceImages()
            }
            imageView.setImageResource(drawableId)
        }
    }

    /**
     * Updates the selected dice images based on the current state of the dice.
     */
    private fun updateSelectedDiceImages(){
        //Make all selected dice image views invisible
        for (item in selectedDiceImageViews){
            item.visibility = View.GONE
        }
        //Set the correct image for the dice in the SelectedDie list. A dice is in that list if the dice is grey in the original dice list (which means it has been selected).
        viewModel.selectedDice.forEachIndexed { index, die ->
            val imageView = selectedDiceImageViews[index]
            val drawableId = drawableDiceIds[die.color]?.get(die.value - 1) ?: throw IllegalArgumentException("Invalid die color or value")
            imageView.setImageResource(drawableId)

            //Make the image views and buttons visible
            imageView.visibility = View.VISIBLE
            binding.chosenDiceTextView.visibility = View.VISIBLE
            binding.submitButton.visibility = View.VISIBLE
            imageView.setImageResource(drawableId)

            imageView.setOnClickListener {
                //Find the dice in the normal dice list and toggle its grey state when the user clicks on a selected dice, and remove it from the selectedDice array
                val correspondingDiceInDiceList = viewModel.dice.find { it.id == die.id }
                correspondingDiceInDiceList?.toggleGreyState()
                viewModel.selectedDice.remove(die)
                imageView.visibility = View.GONE

                //Update the image views for all dice
                updateDiceImages()
                updateSelectedDiceImages()
            }
        }
    }


    /**
     * Rolls the dice and updates their states.
     */
    private fun rollDice() {
        //If throwCounter is 2, it means that the user is on their last throw and that the endOfRound() method should be called
        if (viewModel.throwCounter == 2){
            //Roll the dice and reset their states
            viewModel.dice.forEach { die ->
                die.roll()
                die.resetDie()
            }

            //Update dice image views
            updateDiceImages()
            updateSelectedDiceImages()

            viewModel.throwCounter++
            endOfRound()
        }else if(viewModel.throwCounter < 2){
            //Roll the dice and reset their states
            viewModel.dice.forEach { die ->
                die.roll()
                die.resetDie()
            }

            //Update dice image views
            updateDiceImages()
            updateSelectedDiceImages()

            viewModel.throwCounter++
        }
        updateThrowAndRound()
    }

    /**
     * Handles the submission of chosen dice and checks if the selected value is valid.
     */
    private fun submitClicked() {
        //Get the selected value from the spinner
        val spinnerSelectedValue = diceOptionsSpinner.selectedItem.toString()

        //If the selected value is valid, clear the selected dice and update the dice images
        if (viewModel.pointTracker.calculate(spinnerSelectedValue, viewModel.selectedDice)){
            viewModel.selectedDice.clear()
            viewModel.dice.forEachIndexed { index, die ->
                val imageView = diceImageViews[index]
                if (die.color == DieColor.GREY) {
                    //Change the image to red and set the dice color to red
                    val drawableId = drawableDiceIds[DieColor.RED]?.get(die.value - 1) ?: throw IllegalArgumentException("Invalid die color or value")
                    imageView.setImageResource(drawableId)
                    die.setDiceColor(DieColor.RED)
                    imageView.isClickable = false
                }
            }

            //Make the spinner disabled until the round is over
            viewModel.diceSpinnerEnabled = false
            diceOptionsSpinner.isEnabled = false

            //Update the selected dice image views
            updateSelectedDiceImages()
        }else{  //If the selected value is not valid, show an error toast message
            val messageResId = when (viewModel.selectedPosition) {
                0 -> R.string.no_value_toast
                1 -> R.string.only_less_than_three_toast
                else -> R.string.wrong_math_toast
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Displays the correct buttons to the UI at the end of the round.
     * Also handles the click listener for the done button.
     */
    private fun endOfRound() {
        with(binding){
            //Change visibility states of buttons and text views
            whichDiceTextView.visibility = View.VISIBLE
            nextRoundButton.visibility = View.VISIBLE
            rollButton.visibility = View.GONE

            nextRoundButton.setOnClickListener{
                //If it's the last round, display the final score
                if (viewModel.roundCounter == 9){
                    displayFinalScore()
                }else {
                    //If a value has been chosen from the spinner
                    if (viewModel.selectedPosition != 0) {
                        val view = diceOptionsSpinner.selectedView
                        view.isEnabled = false //Disable the selected value in the spinner
                        viewModel.disableItem(viewModel.selectedPosition)

                        //Reset spinner selection and enable it for the next round
                        diceOptionsSpinner.setSelection(0)
                        diceOptionsSpinner.isEnabled = true
                        viewModel.diceSpinnerEnabled = true

                        //Change visibility states of buttons and text views
                        whichDiceTextView.visibility = View.GONE
                        nextRoundButton.visibility = View.GONE
                        rollButton.visibility = View.VISIBLE
                        chosenDiceTextView.visibility = View.GONE
                        submitButton.visibility = View.GONE

                        //Reset throwCounter and increment roundCounter for next round
                        viewModel.throwCounter = 0
                        viewModel.roundCounter++

                        startNewRound()
                    } else { //If no value has been chosen from the spinner
                        Toast.makeText(this@MainActivity, R.string.no_value_toast, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * Starts a new round by resetting the dice and updating the UI.
     */
    private fun startNewRound() {
        //Clears the selected dice
        viewModel.selectedDice.clear()

        //Update the throw and round information on the UI
        updateThrowAndRound()

        //Reset dice
        viewModel.dice.forEach { die ->
            die.resetDie()
            die.setDefaultValue()
        }

        //Update the image views
        updateDiceImages()
        updateSelectedDiceImages()
    }

    /**
     * Display the final score and round results in the FinalScoreActivity.
     */
    private fun displayFinalScore() {
        //Get the total score and score for each round
        val totalScore = viewModel.pointTracker.getTotalScore()
        val roundResults = viewModel.pointTracker.getRoundResults()

        //Store the total Score in a bundle
        val bundle = Bundle()
        bundle.putInt("FINAL_SCORE", totalScore)

        //Create an intent and set the Bundle as its extra
        val intent = Intent(this, FinalScoreActivity::class.java)
        intent.putExtras(bundle)
        intent.putExtra("ROUND_RESULTS", roundResults) //Pass the round results as an extra to the intent

        //Start the FinalScoreActivity
        startActivity(intent)
    }

}