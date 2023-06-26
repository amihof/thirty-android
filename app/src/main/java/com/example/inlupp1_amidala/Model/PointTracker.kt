package com.example.inlupp1_amidala.Model

/**
 * Author: Amidala Hoffmén
 *
 * Class that tracks the points and calculates the results for each round.
 */
class PointTracker(){
    private var chosenPoint = -1
    private var totalScore = 0
    private val roundResults = IntArray(10)

    /**
     * Calculates the result for the selected point option and the given dice values.
     * Returns true if the calculation is successful, false otherwise.
     */
    fun calculate(selectedPointOption: String, dice: List<Die>): Boolean {
        val valueList = ArrayList<Int>()
        var sum = 0

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
            valueList.add(die.value)
        }

        if (chosenPoint == 3) {
            var allNumbersLessThanThree = true

            for (number in valueList) {
                if (number > 3) {
                    allNumbersLessThanThree = false
                    break
                }
            }
            for (number in valueList) {
                if (number <= 3) {
                    sum += number
                }
            }

            return if (allNumbersLessThanThree) {
                addRoundResult(sum, chosenPoint)
                true
            }else{
                false
            }
        }else{
            for (number in valueList) {
                sum += number
            }
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