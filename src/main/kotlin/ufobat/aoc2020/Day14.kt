package ufobat.aoc2020

import kotlin.streams.toList
import kotlin.system.exitProcess

object Data14 {
    fun getDemoInputTask1() = listOf<String>("mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X",
                                             "mem[8] = 11",
                                             "mem[7] = 101",
                                             "mem[8] = 0")

    fun getDemoInputTask2() = listOf(
"mask = 000000000000000000000000000000X1001X",
"mem[42] = 100",
"mask = 00000000000000000000000000000000X0XX",
"mem[26] = 1"
    )

    fun getPuzzleInput() = this::class.java.getResourceAsStream("/day14_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .toList()
}

// FerryComputerInstruction
interface FCI
data class SetValue(val addr: Long, val value: Long) : FCI
data class Mask(val addMask: Long, val delMask: Long) : FCI {
    override fun toString() = "Mask(${addMask.toString(2)}, ${delMask.toString(2)})"
}

val maskRegex = """mask = (.+)""".toRegex()
val memRegex = """mem\[(\d+)\] = (\d+)""".toRegex()
fun parseInstruction(line: String) : FCI {
    maskRegex.find(line)?.apply {
        val addMask = groupValues[1].replace('X', '0').toLong(2)
        val delMask = groupValues[1].replace('X', '1').toLong(2)
        return Mask(addMask, delMask)
    }
    memRegex.find(line)?.apply {
        val addr = groupValues[1].toLong()
        val value= groupValues[2].toLong()
        return SetValue(addr, value)
    }
    throw IllegalArgumentException("wrong source code")
}

class FerryComputer1() {
    var currentMask: Mask? = null
    private val memory = mutableMapOf<Long, Long>()

    fun invokeInstruction(instruction: FCI) {
        when (instruction) {
            is Mask -> currentMask = instruction
            is SetValue -> {
                var value = instruction.value
                val addMask = currentMask!!.addMask
                val delMask = currentMask!!.delMask
                // println("processing value: " + value.toString(2))
                // println("addMask         : " + addMask.toString(2))
                value = addMask or value
                // println("current value   : " + value.toString(2))
                // println("delMask         : " + delMask.toString(2))
                value = delMask and value
                // println("current value   : " + value.toString(2))
                memory[instruction.addr] = value
            }
        }
    }

    fun memorySum() = memory.values.sum()
}

class FerryComputer2() {
    var currentMask: Mask? = null
    private val memory = mutableMapOf<Long, Long>()
    val floatingMask = 68719476735L

    fun invokeInstruction(instruction: FCI) {
        when (instruction) {
            is Mask -> currentMask = instruction
            is SetValue -> {
                var addr = instruction.addr
                val addMask = currentMask!!.addMask
                val delMask = currentMask!!.delMask

                val floatingMap = (addMask xor delMask) and floatingMask

                // println("setmem force add map: " + addMask.toString(2).padStart(35))
                // println("setmem force del map: " + delMask.toString(2).padStart(35))
                // println("setmem floating map : " + floatingMap.toString(2).padStart(35))
                // println("--")
                // println("current address     : " + addr.toString(2).padStart(35))
                // println("setmem force del map: " + delMask.toString(2).padStart(35))
                addr = addr or addMask
                // println("current address is  : " + addr.toString(2).padStart(35))
                // println("--")
                val allMaps = allFloatingBitmaps(floatingMap)
                allMaps.map {
                    // println("setmem variant fm   : " + it.toString(2).padStart(35))
                    val altAddr = addr xor it
                    // println("alt. address is     : " + altAddr.toString(2).padStart(35) + " = " + altAddr)
                    altAddr
                }.forEach {
                    memory[it] = instruction.value
                }
                // println("---")


            }
        }
    }

    fun allFloatingBitmaps(floatingMap: Long): List<Long> {
        val all = mutableSetOf<Long>(0)

        var map = 0L
        for (pos in 0..35) {
            map = if (map == 0L) 1L else map * 2
            // map *= 2
            // is bit set at pos
            if ((floatingMap and map) != 0L) {
                // create map where bit at pos ( => map value) is set
                all.add(map)
                val addMe = all.map { it or map }
                all.addAll(addMe)
            }
        }
        return all.toList()
    }

    fun memorySum() = memory.values.sum()
}

fun main() {
    val instructions = Data14.getPuzzleInput().map(::parseInstruction)
    val computer1 = FerryComputer1()
    val computer2 = FerryComputer2()

    println(instructions)

//    println(21.toString(2).padStart(10))
//    computer2.allFloatingBitmaps(21).forEach {
//        println(it.toString(2).padStart(10))
//    }
//    println("---")
//    exitProcess(0)

    instructions.forEach {
        computer1.invokeInstruction(it)
        computer2.invokeInstruction(it)
    }

    println(computer2.memorySum())

}

