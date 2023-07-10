package thirty_throws.amho0063.inlupp1_amidala.model

import java.util.Random

/**
 * Author: Amidala Hoffmén
 *
 * Class representing a die.
 */
class Die(){
    var value = 0 // Current value of the die
        private set

    var id = 0  //Identifier of the die
        private set

    var color: DieColor = DieColor.WHITE //Color of the die, default is white

    private val rand = Random() //Random number generator for rolling the die

    private var goingToRoll = true //Flag to indicate if the die is going to roll

    constructor(id: Int) : this() {
        this.id = id //Assign the provided ID to the die
    }

    init {
        roll() //Roll the die when initialized
    }

    /**
     * Rolls the die and sets a random value between 1 and 6.
     * The roll is only performed if the die is going to roll.
     */
    fun roll() {
        if (goingToRoll) {
            value = rand.nextInt(6) + 1
        }
    }

    /**
     * Resets the die's color to white and sets the roll flag to true.
     */
    fun resetDie(){
        color = DieColor.WHITE
        goingToRoll = true
    }

    /**
     * Sets the die's color to the specified color.
     */
    fun setDiceColor(dieColor: DieColor){
        color = dieColor
    }

    /**
     * Sets the die's value to the default value of 1.
     */
    fun setDefaultValue() {
        value = 1
    }

    /**
     * Toggles the die's state between red and white.
     */
    fun toggleRedState(){
        if (color == DieColor.RED){
            color = DieColor.WHITE
            goingToRoll = true
        }else if(color == DieColor.WHITE){
            color = DieColor.RED
            goingToRoll = false
        }
    }

    /**
     * Toggles the die's state between grey and white.
     */
    fun toggleGreyState() {
        if (color == DieColor.GREY){
            color = DieColor.WHITE
        }else if(color == DieColor.WHITE){
            color = DieColor.GREY
        }
    }

}