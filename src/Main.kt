import java.io.File

data class Replica(
    val index: Int, val text: String
)

data class Actor(
    val name: String, val lines: MutableList<Replica> = mutableListOf()
)


fun parseScript(script: List<String>): List<Actor> {
    val actors = mutableListOf<Actor>()

    var currentActor: Actor? = null


    script.forEachIndexed { index, text ->
        val (role, text) = text.split(": ", limit = 2)
        val actor = actors.firstOrNull { it.name == role }
        if (actor != null) {
            actor.lines.add(Replica(index + 1, text))
        } else {
            currentActor = Actor(name = role, lines = mutableListOf(Replica(index + 1, text)))
            actors.add(currentActor!!)
        }
    }

    return actors
}

fun generateOutput(actors: List<Actor>): String {
    val result = StringBuilder()

    for (actor in actors) {
        result.append("${actor.name}:\n")
        actor.lines.forEach { replica ->
            result.append("${replica.index}) ${replica.text}\n")
        }
        result.append("\n")
    }

    return result.toString()
}

fun main() {
    val inputFile = File("input.txt")
    val outputFile = File("output.txt")

    val roles = mutableListOf<String>()
    val textLines = mutableListOf<String>()

    var isRolesSection = true

    inputFile.forEachLine { line ->
        when {
            line.startsWith("roles:") -> isRolesSection = true
            line.startsWith("textLines:") -> isRolesSection = false
            isRolesSection -> roles.add(line.trim())
            else -> textLines.add(line.trim())
        }
    }

    val actors = parseScript(textLines)

    val output = generateOutput(actors)

    println(output)

    outputFile.writeText(output)
    println("Output written to ${outputFile.absolutePath}")
}
