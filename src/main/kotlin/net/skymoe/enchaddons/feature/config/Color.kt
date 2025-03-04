package net.skymoe.enchaddons.feature.config

interface Color {
    val r: Double
    val g: Double
    val b: Double
    val a: Double
}

data class ColorImpl(
    override val r: Double,
    override val g: Double,
    override val b: Double,
    override val a: Double,
) : Color