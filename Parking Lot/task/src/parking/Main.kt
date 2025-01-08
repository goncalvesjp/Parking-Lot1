package parking

import java.util.*
import kotlin.system.exitProcess

fun main() {

    val carParking: CarParking = CarParking()

    while (true) {
        val scanner = Scanner(System.`in`)

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
                "exit" -> exitProcess(0)
                "status" -> carParking.status()
            }
        }
    }
}


class CarParking() {
    var activated: Boolean = false
    var level: Int = 0
    var spots = List<Car?>(0) { null }.toMutableList()

    fun createOrReset(parkSize: Int) {
        activated = true
        level = 0
        spots = List<Car?>(parkSize) { null }.toMutableList()
        println("Created a parking lot with $parkSize spots.")
    }


    fun park(car: Car) {
        if (activated && level < spots.size) {
            var id = 0
            val iterator = spots.iterator()
            do {
                val spot = iterator.next()
                if (spot == null) {
                    spots[id] = car
                    level = id
                    println("${car.carColor} car parked in spot ${id + 1}.")
                    return
                } else {
                    id++
                }
            } while (iterator.hasNext() && id < spots.size)

            println("Sorry, the parking lot is full.")
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }


    fun leave(idSpot: Int) {
        if (activated && level > idSpot - 1) {
            if (spots[idSpot - 1] != null) {
                spots[idSpot - 1] = null
                println("Spot $idSpot is free.")
            } else {
                println("There is no car in spot 1.")
            }
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }


    fun status() {
        if (activated) {
            if (level >= 0) {
                var id = 0
                for (spot in spots) {
                    id++
                    if (spot != null) {
                        println("$id ${spot.registrationNumber} ${spot.carColor}")
                    }
                }
            } else {
                println("Parking lot is empty.")
            }

        } else {
            println("Sorry, a parking lot has not been created.")
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
                println("No cars with color ${color.uppercase()} were found.")
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
                println("No cars with color ${color.uppercase()} were found.")
            }
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }
}


class Car(val carColor: String, val registrationNumber: String)
