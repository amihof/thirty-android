package com.example.inlupp1_amidala.model

/**
 * Author: Amidala Hoffmén
 *
 * Class that tracks the points and calculates the results for each round.
 */
class PointTracker{
    private var chosenPoint = -1 //The selected point option
    private var totalScore = 0 //The total score
    private val roundResults = IntArray(10) //Array to store the round results

    /**
     * Calculates the result for the selected point option and the given dice values.
     * Returns true if the calculation is successful, false otherwise.
     */
    fun calculate(selectedPointOption: String, dice: List<Die>): Boolean {
        val valueList = ArrayList<Int>()  //List to store the values of the dice
        var sum = 0 //Variable to store the sum of dice values

        chosenPoint = when (selectedPointOption) {
            "Low" -> 3
            "4" -> 4
            "5" -> 5
            "6" -> 6
            "7" -> 7
            "8" -> 8
            "9" -> 9
            "10" -> 10
            "11" -> 11
            else -> 12
        }

        dice.forEach { die ->
            valueList.add(die.value)  //Add the value of each die to the list
        }

        //If chosenPoint is 3, flag to check if all numbers are less than or equal to 3
        if (chosenPoint == 3) {
            var allNumbersLessThanThree = true

            for (number in valueList) {
                //If any number is greater than 3, set flag to false
                if (number > 3) {
                    allNumbersLessThanThree = false
                    break
                }
            }
            for (number in valueList) {
                //If a number is less than or equal to 3, add it to the sum
                if (number <= 3) {
                    sum += number
                }
            }

            //Return true if all numbers are less then three, otherwise return false
            return if (allNumbersLessThanThree) {
                addRoundResult(sum, chosenPoint)
                true
            }else{
                false
            }
        }else{ //If the chosen point is not 3
            for (number in valueList) {
                sum += number //Sum all the dice values
            }
            //Add the round result if the sum is equal to the chosen point
            return if (sum == chosenPoint){
                addRoundResult(sum, chosenPoint)
                true
            }else{
                false
            }
        }
    }

    /**
     * Adds the round result to the corresponding index in the roundResults array based on the chosenPoint value.
     * Also updates the totalScore.
     */
    private fun addRoundResult(result: Int, chosenPoint: Int) {
        //Add the result to the corresponding index in the roundResults array
        when(chosenPoint){
            3 -> roundResults[0] = roundResults[0] + result
            4 -> roundResults[1] = roundResults[1] + result
            5 -> roundResults[2] = roundResults[2] + result
            6 -> roundResults[3] = roundResults[3] + result
            7 -> roundResults[4] = roundResults[4] + result
            8 -> roundResults[5] = roundResults[5] + result
            9 -> roundResults[6] = roundResults[6] + result
            10 -> roundResults[7] = roundResults[7] + result
            11 -> roundResults[8] = roundResults[8] + result
            else -> roundResults[9] = roundResults[9] + result
        }

        //Update the total score
        totalScore += result
    }

    /**
     * Returns the roundResults array.
     */
    fun getRoundResults(): IntArray {
        return roundResults
    }

    /**
     * Returns the totalScore.
     */
    fun getTotalScore(): Int {
        return totalScore
    }
}