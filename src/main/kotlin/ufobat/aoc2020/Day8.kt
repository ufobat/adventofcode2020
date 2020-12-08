package ufobat.aoc2020

import java.lang.Integer.max
import kotlin.streams.toList

data class ComputationResult(val instructionPos: Int, val accumulator: Int, val completed: Boolean)

class Computer(private val instruction: List<Instruction>) {
    private var visitedInstruction = mutableSetOf<Int>()
    private var accumulator = 0
    private var instructionCounter = 0
    private var maxInstructionCounter = 0
    private var exchangeInstructions = mutableListOf<Int>()

    fun reset() {
        this.instructionCounter = 0
        this.maxInstructionCounter = 0
        this.accumulator = 0
        this.visitedInstruction = mutableSetOf()
    }

    fun run(
            storeExchangeInstructions: Boolean = false,
            switchInstructionAt: Int = -1
    ): ComputationResult {
        reset()
        while (true) {
            // check for double visit
            if (visitedInstruction.contains(instructionCounter))
                return ComputationResult(instructionCounter, accumulator, false)

            // regular termination
            if (instructionCounter >= instruction.count())
                return ComputationResult(instructionCounter, accumulator, true)

            visitedInstruction.add(instructionCounter)

            // get instruction
            val instruction =
                    if (instructionCounter == switchInstructionAt) instruction[instructionCounter].switchCommand()
                    else instruction[instructionCounter]

            // debug the computer
            // maxInstructionCounter = max(maxInstructionCounter, instructionCounter)
            // println("${instructionCounter +1} (${maxInstructionCounter + 1})-> $instruction")

            if (storeExchangeInstructions) {
                if (instruction.cmd == "jmp" || instruction.cmd == "nop")
                exchangeInstructions.add(instructionCounter)
            }

            // process instruction
            when (instruction.cmd) {
                "nop" -> {}
                "jmp" -> { instructionCounter += instruction.param - 1 }
                "acc" -> { accumulator += instruction.param }
            }

            // next code
            instructionCounter++
        }
    }

    fun debug() {
        run(true)
        val exchangeInstructions = this.exchangeInstructions

        exchangeInstructions.forEach { switchInstructionAt ->
            val result = run(false, switchInstructionAt )
            if (result.completed)
                println("$switchInstructionAt $result")
        }
    }
}

data class Instruction(val cmd: String, val param: Int) {
    fun switchCommand() = when (cmd) {
            "jmp" -> Instruction("nop", param)
            "nop" -> Instruction("jmp", param)
            else -> this
    }
}

class Day8 {
    private val instructionRegex = """(acc|nop|jmp) (\+|\-)(\d+)""".toRegex()

    fun getPuzzleInput() =
            this::class.java.getResourceAsStream("/day8_puzzle_input.txt")
                    .bufferedReader()
                    .lines()
                    .toList()

    fun getDemoInput() =
            this::class.java.getResourceAsStream("/day8_demo_input.txt")
                    .bufferedReader()
                    .lines()
                    .toList()


    fun parseInstruction(line: String): Instruction {
        val match = instructionRegex.find(line)
                    ?: throw IllegalArgumentException("can not understand $line")
        val cmd = match.groupValues[1]
        val param = match.groupValues[3].toInt() * if (match.groupValues[2] == "-") -1 else 1
        return Instruction(cmd, param)
    }

    fun buildDemoComputer(): Computer {
        val instructions = getDemoInput().map { parseInstruction(it) }
        return Computer(instructions)
    }

    fun buildComputer(): Computer {
        val instructions = getPuzzleInput().map { parseInstruction(it) }
        return Computer(instructions)
    }
}

fun main() {
    val day8 = Day8()
    val demoComputer = day8.buildDemoComputer()
    val demoResult = demoComputer.run()
    println("Demo Computer: ")
    println(demoResult.instructionPos)
    println(demoResult.accumulator)

    val computer = day8.buildComputer()
    val result = computer.run()
    println("Task 1:")
    println(result.instructionPos)
    println(result.accumulator)
    println("-----")

    computer.debug()
}