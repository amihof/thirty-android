package com.example.inlupp1_amidala.ViewModel

import androidx.lifecycle.ViewModel
import com.example.inlupp1_amidala.Model.Die
import com.example.inlupp1_amidala.Model.PointSpinner
import com.example.inlupp1_amidala.Model.PointTracker

class MyViewModel : ViewModel() {
    var diceSpinnerEnabled: Boolean = true

    //ArrayList with Die objects that hold the dice
    val dice = ArrayList<Die>()

    //ArrayList with Die objects that are grey
    val greyInvisDie = ArrayList<Die>()

    //counters for throws and round
    var throwCounter = 0
    var roundCounter = 0

    //variables that keeps track of which position on the spinner is selected and which position is the default
    var selectedPosition = 0

    //Class that initializes the drop-down menu (spinner)
    lateinit var adapter: PointSpinner

    lateinit var pointTracker: PointTracker

    fun initializePointTracker() {
        if (!::pointTracker.isInitialized) {
            pointTracker = PointTracker()
        }
    }

    // Initialize the dice and add dice to the arraylist
    fun initializeDice() {
        for (i in 0..5) {
            dice.add(Die(i))
        }
    }

    var disabledPositions: MutableList<Int> = mutableListOf()

    fun disableItem(position: Int) {
        disabledPositions.add(position)
    }

}