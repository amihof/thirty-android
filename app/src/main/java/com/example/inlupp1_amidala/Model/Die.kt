package com.example.inlupp1_amidala.Model

import java.util.Random

/**
 * Author: Amidala Hoffmén
 *
 * Class representing a die.
 */
class Die(){
    var value = 0
        private set

    var id = 0
        private set

    var color: DieColor = DieColor.WHITE

    private val rand = Random();

    private var goingToRoll = true // Add a boolean flag to know if this die is going to roll

    constructor(id: Int) : this() {
        this.id = id
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

    init {
        roll()
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