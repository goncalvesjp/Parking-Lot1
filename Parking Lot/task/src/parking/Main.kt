package parking

import java.util.*

fun main() {

    val carParking: CarParking = CarParking()

    val scanner = Scanner(System.`in`)

    while (true) {
        val input = scanner.nextLine()
        val inputList = input.split(" ")

        when (inputList.size) {
            3 -> {
                if (inputList[0].trim() == "park") {
                    val registrationNumber = inputList[1].trim()
                    val color = inputList[2].trim()
                    val car = Car(color, registrationNumber)
                    carParking.park(car)
                }
            }

            2 -> when (inputList[0].trim()) {
                "leave" -> carParking.leave(inputList[1].toInt())
                "create" -> carParking.createOrReset(inputList[1].toInt())
                "spot_by_color" -> carParking.spotByColor(inputList[1])
                "reg_by_color" -> carParking.regByColor(inputList[1])
                "spot_by_reg" -> carParking.spotByReg(inputList[1])
            }

            1 -> when (inputList[0].trim()) {
                "exit" -> return
                "status" -> carParking.status()
            }
        }
    }
}


class CarParking() {
    var activated: Boolean = false
    var spots = List<Car?>(0) { null }.toMutableList()

    fun createOrReset(parkSize: Int) {
        activated = true
        spots = List<Car?>(parkSize) { null }.toMutableList()
        println("Created a parking lot with $parkSize spots.")
    }


    fun park(car: Car) {
        if (!activated) {
            println("Sorry, a parking lot has not been created.")
            return
        }
        val id = spots.indexOfFirst { it == null }
        if (id == -1) {
            println("Sorry, the parking lot is full.")
        } else {
            spots[id] = car
            println("${car.carColor} car parked in spot ${id + 1}.")
        }
    }


    fun leave(idSpot: Int) {
        if (!activated) {
            println("Sorry, a parking lot has not been created.")
        } else if (idSpot !in 1..spots.size || spots[idSpot - 1] == null) {
            println("There is no car in spot $idSpot.")
        } else {
            spots[idSpot - 1] = null
            println("Spot $idSpot is free.")
        }
    }


    fun status() {
        if (!activated) {
            println("Sorry, a parking lot has not been created.")
        } else if (spots.all { it == null }) {
            println("Parking lot is empty.")
        } else {
            for ((i, spot) in spots.withIndex()) {
                if (spot != null) {
                    println("${i + 1} ${spot.registrationNumber} ${spot.carColor}")
                }
            }
        }
    }

    fun spotByColor(color: String) {
        if (activated && spots.size > 0) {
            val myList = mutableListOf<Int>()
            for (i in spots.indices) {
                if (spots[i]?.carColor.equals(color, true)) {
                    myList.add(i + 1)
                }
            }
            if (myList.isNotEmpty()) {
                println(myList.joinToString(prefix = "", postfix = "", separator = ", "))
            } else {
                println("No cars with color $color were found.")
            }
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }


    fun spotByReg(registration: String) {
        if (activated && spots.size > 0) {
            val myList = mutableListOf<Int>()
            for (i in spots.indices) {
                if (spots[i]?.registrationNumber.equals(registration, true)) {
                    myList.add(i + 1)
                }
            }
            if (myList.isNotEmpty()) {
                println(myList.joinToString(prefix = "", postfix = "", separator = ", "))
            } else {
                println("No cars with registration number $registration were found.")
            }
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }

    fun regByColor(color: String) {
        if (activated && spots.size > 0) {
            val spotsFiltered = spots.stream()
                .filter { spot -> spot?.carColor.equals(color, ignoreCase = true) }
                .toList()

            if (spotsFiltered.isNotEmpty()) {
                println(spotsFiltered.map { s -> s?.registrationNumber ?: "" }
                    .joinToString(prefix = "", postfix = "", separator = ", "))
            } else {
                println("No cars with color $color were found.")
            }
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }
}


class Car(val carColor: String, val registrationNumber: String)
