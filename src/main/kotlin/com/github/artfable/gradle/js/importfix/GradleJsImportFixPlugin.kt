package com.github.artfable.gradle.js.importfix

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.lang.IllegalArgumentException

class GradleJsImportFixPlugin : Plugin<Project> {

    val IMPORT_REGEXP = Regex("import\\s.*'([^\\./].*)';?")

    override fun apply(project: Project) {
        val config = project.extensions.create("jsImport", GradleJsImportFixExtension::class.java)

        project.task("jsImportFix").doFirst {
            if (config.directory.isNullOrBlank()) {
                throw IllegalArgumentException("Directory should be provided!")
            }

            val directory = File(config.directory)

            if (!directory.isDirectory) {
                throw IllegalArgumentException("${directory.absolutePath} is incorrect directory!")
            }

            replacementStep(directory)
        }
    }

    fun replacementStep(dir: File, prefix: String = "./") {
        for (file in dir.listFiles()) {
            if (file.isFile && file.name.endsWith(".js")) {
                val tempFile = File.createTempFile(file.nameWithoutExtension, ".js")
                file.forEachLine { line ->
                    var newLine = line
                    if (IMPORT_REGEXP.matches(line)) {
                        IMPORT_REGEXP.matchEntire(line)?.groupValues?.get(1)?.let { path ->
                            newLine = line.replace(path, prefix + path)
                        }
                    }
                    newLine += '\n'
                    tempFile.appendText(newLine)
                }
                tempFile.copyTo(file, true)
            }

            if (file.isDirectory) {
                replacementStep(file, if (prefix == "./") "../" else "$prefix../")
            }
        }
    }
}

open class GradleJsImportFixExtension {
    var directory: String? = null
}