import kotlin.math.floor
import kotlin.math.pow

private class Computer(
    var registerA: Long,
    var registerB: Long,
    var registerC: Long,
    var program: List<Int>,
    var pointer: Int = 0
)

private fun parseInput(input: List<String>): Computer {
    val regA = input[0].split(": ")[1].toLong()
    val regB = input[1].split(": ")[1].toLong()
    val regC = input[2].split(": ")[1].toLong()

    val program = input[4].split(": ")[1].split(",").map { it.toInt() }

    return Computer(regA, regB, regC, program)
}

private fun octalPrint(computer: Computer, outputBuffer: List<Int>) {
    print("A: ${computer.registerA.toString(8)}, ")
    print("B: ${computer.registerB.toString(8)}, ")
    print("C: ${computer.registerC.toString(8)}, ")
    println("pointer: ${computer.pointer}, out: $outputBuffer")
}

private fun compute(computer: Computer): List<Int> {
    val outputBuffer = mutableListOf<Int>()

    fun comboOperand(op: Int) = when(op) {
        in 0..3 -> op.toLong()
        4 -> computer.registerA
        5 -> computer.registerB
        6 -> computer.registerC
        else -> throw IllegalStateException("Combo operand $op is not allowed")
    }

    fun adv(operand: Int) {
        val denominator = 2.0.pow(comboOperand(operand).toDouble())
        val numerator = computer.registerA.toDouble()
        computer.registerA = floor(numerator / denominator).toLong()
    }

    fun bdv(operand: Int) {
        val denominator = 2.0.pow(comboOperand(operand).toDouble())
        val numerator = computer.registerA.toDouble()
        computer.registerB = floor(numerator / denominator).toLong()
    }

    fun cdv(operand: Int) {
        val denominator = 2.0.pow(comboOperand(operand).toDouble())
        val numerator = computer.registerA.toDouble()
        computer.registerC = floor(numerator / denominator).toLong()
    }

    fun bxl(operand: Int) {
        computer.registerB = operand.toLong() xor computer.registerB
    }

    fun bst(operand: Int) {
        computer.registerB = comboOperand(operand) % 8
    }

    fun jnz(operand: Int) {
        if (computer.registerA != 0L) {
            computer.pointer = operand - 2 // because it will get moved on 2 later
        }
    }

    fun bxc() {
        computer.registerB = computer.registerB xor computer.registerC
    }

    fun out(operand: Int) {
        outputBuffer.add((comboOperand(operand) % 8).toInt())
    }

    while (computer.pointer < computer.program.size) {
//        octalPrint(computer, outputBuffer)

        val instruction = computer.program[computer.pointer]
        val operand = computer.program[computer.pointer + 1]

        when(instruction) {
            0 -> adv(operand)
            1 -> bxl(operand)
            2 -> bst(operand)
            3 -> jnz(operand)
            4 -> bxc()
            5 -> out(operand)
            6 -> bdv(operand)
            7 -> cdv(operand)
            else -> throw IllegalStateException("Invalid opcode: $instruction")
        }

        computer.pointer += 2
    }

    return outputBuffer
}


private fun findRegA(computer: Computer): Long {

    val max = computer.program.size

    data class Step(val aval: Long, val level: Int)
    val queue = ArrayDeque<Step>()

    queue.add(Step(0L, 1))

    while(queue.isNotEmpty()) {
        val nextStep = queue.removeFirst()

        for (i in 0..7) {
            val regAStart = nextStep.aval or i.toLong()
            computer.registerA = regAStart
            computer.pointer = 0
            val output = compute(computer)
            if (output.size == nextStep.level) {
                if (output.joinToString(",") == computer.program.takeLast(nextStep.level).joinToString(",")) {
                    if (nextStep.level == max) {
                        return regAStart
                    }
                    queue.add(Step(regAStart shl 3, nextStep.level +  1))
                }
            }

        }
    }

    throw Exception("blah")
}


fun main() {
    val day = "17"


    fun part1(input: List<String>): String {
        val computer = parseInput(input)
        val result = compute(computer).joinToString(",")
        return result
    }

    fun part2(input: List<String>): Long {
        val computer = parseInput(input)
        val result = findRegA(computer)
        return result
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    val testInput2 = readInput("Day${day}_test2")
    check(part2(testInput2) == 117440L)
    part2(input).println()
}
