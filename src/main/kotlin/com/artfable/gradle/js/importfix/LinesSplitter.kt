package com.artfable.gradle.js.importfix

/**
 * @author aveselov
 * @since 05/09/2021
 */
class LinesSplitter(val line: String) {
    private var quote = false
    private var doubleQuote = false
    private var multiQuote = false

    val result = splitLines(line)

    private fun splitLines(line: String): String {
        var newLine = ""
        line.forEach { ch ->
            newLine += ch
            when(ch) {
                '\'' -> quote = !(isStringOpen() || quote)
                '"' -> doubleQuote = !(isStringOpen() || doubleQuote)
                '`' -> multiQuote = !(isStringOpen() || multiQuote)
                ';' -> if (!isStringOpen()) { newLine += '\n' }
            }
        }

        return newLine
    }

    private fun isStringOpen(): Boolean {
        return quote || doubleQuote || multiQuote;
    }
}