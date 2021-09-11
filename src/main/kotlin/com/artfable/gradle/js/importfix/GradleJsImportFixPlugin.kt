package com.artfable.gradle.js.importfix

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import java.io.File
import java.lang.IllegalArgumentException

private val IMPORT_REGEXP = Regex("(?:import|export)\\s?[^\"']*(?:from)?\\s?['\"]([^./\"'][^\"']*)['\"];?")
private val IMPORT_LINE_REGEXP = Regex("(?:import|export)\\s?[^\"']*(?:from)?\\s?['\"][^./\"'][^\"']*['\"];?")

class GradleJsImportFixPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val config = project.extensions.create("jsImport", GradleJsImportFixExtension::class.java)

        project.task("jsImportFix").doFirst {
            if (config.directory.isNullOrBlank()) {
                throw IllegalArgumentException("Directory should be provided!")
            }

            val directory = File(config.directory!!)

            if (!directory.isDirectory) {
                throw IllegalArgumentException("${directory.absolutePath} is incorrect directory!")
            }

            replacementStep(logger, directory)
        }.group = "frontend"
    }

    private fun replacementStep(logger: Logger, dir: File, prefix: String = "./") {
        for (file in dir.listFiles() ?: arrayOf()) {
            if (file.isFile && file.name.endsWith(".js")) {
                logger.debug("Processing $file")
                val tempFile = File.createTempFile(file.nameWithoutExtension + "_temp", ".js")
                file.forEachLine { line ->
                    var newLine = line
                    if (IMPORT_LINE_REGEXP.findAll(line).any()) {
                        val splitLines = LinesSplitter(line).result

                        newLine = splitLines.lines().map { splitLine ->
                            IMPORT_REGEXP.matchEntire(splitLine)?.groupValues?.get(1)?.let { splitLine.replace(it, prefix + it) } ?: splitLine
                        }.joinToString("\n")
                    }
                    newLine += '\n'
                    tempFile.appendText(newLine)
                }
                tempFile.copyTo(file, true)
            }

            if (file.isDirectory) {
                replacementStep(logger, file, if (prefix == "./") "../" else "$prefix../")
            }
        }

        if (dir.listFiles { path -> path.name.equals("index.js") }?.isEmpty() == true && dir.listFiles { path -> path.name.equals(dir.name + ".js") }?.isEmpty() == false) {
            val indexFile = File(dir.absolutePath + File.separator + "index.js")
            indexFile.createNewFile()

            indexFile.writeText("import \"./${dir.name}.js\";\nexport * from \"./${dir.name}.js\";")
        }
    }
}

open class GradleJsImportFixExtension {
    var directory: String? = null
}