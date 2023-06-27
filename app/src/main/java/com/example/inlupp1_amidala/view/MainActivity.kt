package com.example.inlupp1_amidala.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.inlupp1_amidala.viewmodel.MyViewModel
import com.example.inlupp1_amidala.model.DieColor
import com.example.inlupp1_amidala.model.PointSpinnerAdapter
import com.example.inlupp1_amidala.R
import com.example.inlupp1_amidala.databinding.ActivityMainBinding

/**
 * Author: Amidala Hoffmén
 *
 * The main activity that manages the game and the user interface.
 * It handles dice rolling, displaying and updating UI elements, and managing user interactions.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MyViewModel

    //ArrayList that holds the ImageView of the dice
    private val diceImageViews = ArrayList<ImageView>()

    //ArrayList that holds the ImageView of the invisible dice
    private val invisDiceImageViews = ArrayList<ImageView>()

    //the drop-down menu (spinner)
    lateinit var diceOptionsSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        if(viewModel.dice.isEmpty()){
            //initialize the dice and add dice to the arraylist
            viewModel.initializeDice()
        }

        // Initialize the image views and add them to the arraylist
        initializeViews()

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
            endOfRound()
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

            invisDiceImageViews.addAll(
                listOf(
                    imageView,
                    imageView2,
                    imageView3,
                    imageView4,
                    imageView5,
                    imageView6
                )
            )
        }
    }

    /**
     * Updates the display of the current throw and round in the UI.
     */
    private fun updateThrowAndRound() {
        with(binding) {
            val throwMessage = "Throw ${if(viewModel.throwCounter == 3) 3 else viewModel.throwCounter+1} of 3"
            whichThrow.text = throwMessage

            val roundMessage = "Round ${viewModel.roundCounter + 1} of 10"
            whichRound.text = roundMessage
        }
    }

    /**
     * Initializes the spinner and sets its item selection listener to handle user choices.
     */
    private fun initializeSpinner() {
        val choices = resources.getStringArray(R.array.choices_array).toList()
        diceOptionsSpinner = binding.spinner
        diceOptionsSpinner.setSelection(viewModel.selectedPosition)
        diceOptionsSpinner.isEnabled = viewModel.diceSpinnerEnabled

        diceOptionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get the selected position from the spinner
                viewModel.selectedPosition = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}//do nothing
        }

        viewModel.adapter = PointSpinnerAdapter(this, R.layout.spinner_text_custom, choices, viewModel)
        viewModel.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        diceOptionsSpinner.adapter = viewModel.adapter
    }

    /**
     * Updates the visible dice images based on the current state of the dice.
     */
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
                        when (die.color) {
                            DieColor.WHITE -> {
                                viewModel.greyInvisDie.add(die)
                                die.setDiceColor(DieColor.GREY)
                            }
                            DieColor.GREY -> {
                                viewModel.greyInvisDie.remove(die)
                                die.setDiceColor(DieColor.WHITE)
                            }
                            DieColor.RED -> {
                                imageView.isClickable = false
                            }
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

    /**
     * Updates the invisible dice images based on the current state of the dice.
     */
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
            binding.textView.visibility = View.VISIBLE
            binding.button2.visibility = View.VISIBLE
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


    /**
     * Rolls the dice and updates their states accordingly.
     *
     * @param src The View that triggered the dice roll.
     */
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
            endOfRound()
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

    /**
     * Handles the submission of chosen dice and checks if the selected value is valid.
     *
     * @param view The View that triggered the submit action.
     */
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

    /**
     * Displays the correct buttons to the UI
     */
    private fun endOfRound() {
        with(binding){
            doneText.visibility = View.VISIBLE
            doneButton.visibility = View.VISIBLE
            button.visibility = View.GONE

            doneButton.setOnClickListener{
                if (viewModel.roundCounter == 9){
                    displayFinalScore()
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
                        button.visibility = View.VISIBLE
                        textView.visibility = View.GONE
                        button2.visibility = View.GONE
                        viewModel.throwCounter = 0
                        viewModel.roundCounter++
                        startNewRound()
                    } else {
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
        viewModel.greyInvisDie.clear()
        updateThrowAndRound()
        viewModel.dice.forEach { die ->
            die.resetDie()
            die.setDefaultValue()
        }
        updateDiceImages()
        updateInvisDiceImages()
    }

    /**
     * Display the final score and round results in the FinalScoreActivity.
     */
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