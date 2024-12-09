private data class Rule(val a: Int, val b: Int)
private data class Update(val pages: List<Int>) {
    fun middlePage() = pages[pages.size / 2]
}

// returns (rules, updates) as two lists
private fun parseInput(input: List<String>): Pair<List<Rule>, List<Update>> {
    var inRules = true
    val rules = mutableListOf<Rule>()
    val updates = mutableListOf<Update>()

    input.forEach { line ->
        if (line.trim().isEmpty()) {
            inRules = false
        } else if (inRules) {
            val pages = line.trim().split('|').map { it.toInt() }
            rules.add(Rule(pages[0], pages[1]))
        } else {
            updates.add(Update(line.trim().split(',').map { it.toInt() }.toList()))
        }
    }

    return Pair(rules, updates)
}

// returns the middle page number of the Update, or 0 if invalid
private fun checkUpdateIsValid(rules: List<Rule>, update: Update): Int {
    for (rule in rules) {
        val ia = update.pages.indexOf(rule.a)
        val ib = update.pages.indexOf(rule.b)
        // If either is not found, rule doesn't apply.
        if (ia >= 0 && ib >= 0 && ia > ib) {
            return 0 // rule not met
        }
    }

    return update.middlePage()
}

// returns the fixed Update
private fun fixUpdate(rules: List<Rule>, update: Update): Update {
    val pages = ArrayList(update.pages) // take a copy
    while (checkUpdateIsValid(rules, Update(pages)) == 0) {
        for (rule in rules) {
            val ia = pages.indexOf(rule.a)
            val ib = pages.indexOf(rule.b)
            // If either is not found, rule doesn't apply.
            if (ia >= 0 && ib >= 0 && ia > ib) {
                // rule is broken, swap elements:
                val tmp = pages[ia]
                pages[ia] = pages[ib]
                pages[ib] = tmp
            }
        }
    }

    return Update(pages)
}

fun main() {
    val day = "05"

    fun part1(input: List<String>): Int {
        val (rules, updates) = parseInput(input)
        return updates.sumOf { checkUpdateIsValid(rules, it) }
    }

    fun part2(input: List<String>): Int {
        val (rules, updates) = parseInput(input)
        return updates.filter { checkUpdateIsValid(rules, it) == 0 }
            .map { fixUpdate(rules, it) }
            .sumOf { it.middlePage() }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 143)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day${day}")
    part1(input).println()


    check(part2(testInput) == 123)
    part2(input).println()
}
