package net.skymoe.enchaddons.api

import kotlin.random.Random

interface Template {
    operator fun set(key: String, value: Any?)

    fun format(): String
}

typealias TemplateProvider = (String) -> Template

fun Template.setStyles() {
    "r0123456789abcdefklmno".forEach { this["$it"] = "\u00a7$it" }
}

data object RandomGenerator {
    val braille get() = Char(Random.nextInt(256) + 10240)
}

fun Template.setRandom() {
    this["rng"] = RandomGenerator
}

fun Template.setDefault() {
    setRandom()
    setStyles()
}