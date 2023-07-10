package thirty_throws.amho0063.inlupp1_amidala.viewmodel

import androidx.lifecycle.ViewModel
import thirty_throws.amho0063.inlupp1_amidala.model.Die
import thirty_throws.amho0063.inlupp1_amidala.model.PointSpinnerAdapter
import thirty_throws.amho0063.inlupp1_amidala.model.PointTracker

/**
 * Author: Amidala Hoffmén
 *
 * ViewModel class that holds the data and logic for the game.
 */
class MyViewModel : ViewModel() {
    //Indicates whether the dice spinner is enabled or disabled
    var diceSpinnerEnabled: Boolean = true

    //ArrayList with Die objects that hold the dice
    val dice = ArrayList<Die>()

    //ArrayList with Die objects that are selected
    val selectedDice = ArrayList<Die>()

    //Counters for throws and round
    var throwCounter = 0
    var roundCounter = 0

    //Variables that keeps track of which position on the spinner is selected and which position is the default
    var selectedPosition = 0

    //Adapter for the point spinner
    lateinit var adapter: PointSpinnerAdapter

    //PointTracker to track points
    lateinit var pointTracker: PointTracker

    //List of disabled positions in the spinner
    var disabledPositions: MutableList<Int> = mutableListOf()

    /**
     * Initializes the PointTracker if it has not been initialized yet.
     */
    fun initializePointTracker() {
        if (!::pointTracker.isInitialized) {
            pointTracker = PointTracker()
        }
    }

    /**
     * Initializes the dice and adds dice to the ArrayList.
     */
    fun initializeDice() {
        for (i in 0..5) {
            dice.add(Die(i))
        }
    }

    /**
     * Disables an item at the specified position in the spinner.
     */
    fun disableItem(position: Int) {
        disabledPositions.add(position)
    }

}