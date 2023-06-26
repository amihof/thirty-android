package com.example.inlupp1_amidala.Model

import java.util.Random

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

    fun roll() {
        if (goingToRoll) {
            value = rand.nextInt(6) + 1
        }
    }

    init {
        roll()
    }

    fun resetDie(){
        color = DieColor.WHITE
        goingToRoll = true
    }

    fun setDiceColor(dieColor: DieColor){
        color = dieColor
    }

    fun setDefaultValue() {
        value = 1
    }

    fun toggleRedState(){
        if (color == DieColor.RED){
            color = DieColor.WHITE
            goingToRoll = true
        }else if(color == DieColor.WHITE){
            color = DieColor.RED
            goingToRoll = false
        }
    }

    fun toggleGreyState() {
        if (color == DieColor.GREY){
            color = DieColor.WHITE
        }else if(color == DieColor.WHITE){
            color = DieColor.GREY
        }
    }

}