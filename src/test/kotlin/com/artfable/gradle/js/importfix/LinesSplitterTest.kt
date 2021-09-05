package com.artfable.gradle.js.importfix

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author aveselov
 * @since 05/09/2021
 */
internal class LinesSplitterTest {

    @Test
    fun testSplitter() {
        val linesSplitter =
            LinesSplitter("import{ReactiveElement as t}from\"@lit/reactive-element\";export*from\"@lit/reactive-element\";import{render as e,noChange as i}from\"lit-html\";export*from\"lit-html\";")

        assertEquals("import{ReactiveElement as t}from\"@lit/reactive-element\";\n" +
                "export*from\"@lit/reactive-element\";\n" +
                "import{render as e,noChange as i}from\"lit-html\";\n" +
                "export*from\"lit-html\";\n", linesSplitter.result)
    }
}