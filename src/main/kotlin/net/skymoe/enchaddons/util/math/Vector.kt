package net.skymoe.enchaddons.util.math

import kotlinx.serialization.Serializable
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

sealed interface Vector<TV : Number, T : Vector<TV, T>> {
    val length: Double

    val lengthSquared: TV

    operator fun plus(vec: T): T

    operator fun minus(vec: T): T

    operator fun times(value: TV): T

    operator fun times(vec: T): TV

    operator fun unaryPlus(): T

    operator fun unaryMinus(): T

    infix fun min(other: T): T

    infix fun max(other: T): T

    infix fun allLess(other: T): Boolean

    infix fun allLessEqual(other: T): Boolean

    infix fun areaTo(other: T) = min(other) to max(other)
}

fun <T : Vector<Double, T>> lerp(
    from: T,
    to: T,
    progress: Double,
) = from + (to - from) * progress

typealias Area<T> = Pair<T, T>

operator fun <TV : Number, T : Vector<TV, T>> Area<T>.contains(vec: T) = first allLessEqual vec && vec allLess second

@Serializable
data class Vec2I(
    val x: Int,
    val y: Int,
) : Vector<Int, Vec2I> {
    override operator fun plus(vec: Vec2I) = Vec2I(x + vec.x, y + vec.y)

    override operator fun minus(vec: Vec2I) = Vec2I(x - vec.x, y - vec.y)

    override operator fun times(value: Int) = Vec2I(x * value, y * value)

    override operator fun times(vec: Vec2I) = x * vec.x + y * vec.y

    override operator fun unaryPlus() = this

    override operator fun unaryMinus() = Vec2I(-x, -y)

    fun plus(
        dX: Int = 0,
        dY: Int = 0,
    ) = Vec2I(x + dX, y + dY)

    fun times(
        nX: Int = 1,
        nY: Int = 1,
    ) = Vec2I(x * nX, y * nY)

    override val lengthSquared get() = x * x + y * y

    override val length get() = sqrt(lengthSquared.double)

    override fun min(other: Vec2I) = Vec2I(minOf(x, other.x), minOf(y, other.y))

    override fun max(other: Vec2I) = Vec2I(maxOf(x, other.x), maxOf(y, other.y))

    override fun allLess(other: Vec2I) = x < other.x && y < other.y

    override fun allLessEqual(other: Vec2I) = x <= other.x && y <= other.y
}

inline val Vec2I.asVec2D get() = Vec2D(x.double, y.double)

inline val Vec2I.asCenterVec2D get() = Vec2D(x + 0.5, y + 0.5)

inline val Vec2I.asMaxVec2D get() = Vec2D(x + 1.0, y + 1.0)

typealias Area2I = Area<Vec2I>

val Area2I.iterable2I: Iterable<Vec2I>
    get() {
        return object : Iterable<Vec2I> {
            override fun iterator(): Iterator<Vec2I> {
                return iterator {
                    repeat(second.y - first.y) { dy ->
                        repeat(second.x - first.x) { dx ->
                            yield(first + Vec2I(dx, dy))
                        }
                    }
                }
            }
        }
    }

@Serializable
data class Vec2D(
    val x: Double,
    val y: Double,
) : Vector<Double, Vec2D> {
    constructor(x: Float, y: Float) : this(x.double, y.double)

    override operator fun plus(vec: Vec2D) = Vec2D(x + vec.x, y + vec.y)

    override operator fun minus(vec: Vec2D) = Vec2D(x - vec.x, y - vec.y)

    override operator fun times(value: Double) = Vec2D(x * value, y * value)

    override operator fun times(vec: Vec2D) = x * vec.x + y * vec.y

    operator fun div(value: Double) = Vec2D(x / value, y / value)

    override operator fun unaryPlus() = this

    override operator fun unaryMinus() = Vec2D(-x, -y)

    fun plus(
        dX: Double = 0.0,
        dY: Double = 0.0,
    ) = Vec2D(x + dX, y + dY)

    fun times(
        nX: Double = 1.0,
        nY: Double = 1.0,
    ) = Vec2D(x * nX, y * nY)

    fun div(
        nX: Double = 1.0,
        nY: Double = 1.0,
    ) = Vec2D(x / nX, y / nY)

    override val lengthSquared get() = x * x + y * y

    override val length get() = sqrt(lengthSquared.double)

    override fun min(other: Vec2D) = Vec2D(minOf(x, other.x), minOf(y, other.y))

    override fun max(other: Vec2D) = Vec2D(maxOf(x, other.x), maxOf(y, other.y))

    override fun allLess(other: Vec2D) = x < other.x && y < other.y

    override fun allLessEqual(other: Vec2D) = x <= other.x && y <= other.y
}

inline val Vec2D.asVec2I get() = Vec2I(x.int, y.int)

inline val Vec2D.asFloorVec2I get() = Vec2I(x.floorInt, y.floorInt)

inline val Vec2D.asCeilVec2I get() = Vec2I(x.ceilInt, y.ceilInt)

fun unitVec(radian: Double) = Vec2D(cos(radian), sin(radian))

typealias Area2D = Area<Vec2D>

@Serializable
data class Vec3I(
    val x: Int,
    val y: Int,
    val z: Int,
) : Vector<Int, Vec3I> {
    override operator fun plus(vec: Vec3I) = Vec3I(x + vec.x, y + vec.y, z + vec.z)

    override operator fun minus(vec: Vec3I) = Vec3I(x - vec.x, y - vec.y, z - vec.z)

    override operator fun times(value: Int) = Vec3I(x * value, y * value, z * value)

    override operator fun times(vec: Vec3I) = x * vec.x + y * vec.y + z * vec.z

    override operator fun unaryPlus() = this

    override operator fun unaryMinus() = Vec3I(-x, -y, -z)

    fun plus(
        dX: Int = 0,
        dY: Int = 0,
        dZ: Int = 0,
    ) = Vec3I(x + dX, y + dY, z + dZ)

    fun times(
        nX: Int = 1,
        nY: Int = 1,
        nZ: Int = 1,
    ) = Vec3I(x * nX, y * nY, z * nZ)

    fun cross(vec: Vec3I) = Vec3I(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x)

    override val lengthSquared get() = x * x + y * y + z * z

    override val length get() = sqrt(lengthSquared.double)

    override fun min(other: Vec3I) = Vec3I(minOf(x, other.x), minOf(y, other.y), minOf(z, other.z))

    override fun max(other: Vec3I) = Vec3I(maxOf(x, other.x), maxOf(y, other.y), maxOf(z, other.z))

    override fun allLess(other: Vec3I) = x < other.x && y < other.y && z < other.z

    override fun allLessEqual(other: Vec3I) = x <= other.x && y <= other.y && z <= other.z
}

inline val Vec3I.asVec3D get() = Vec3D(x.double, y.double, z.double)

inline val Vec3I.asCenterVec3D get() = Vec3D(x + 0.5, y + 0.5, z + 0.5)

inline val Vec3I.asMaxVec3D get() = Vec3D(x + 1.0, y + 1.0, z + 1.0)

typealias Area3I = Area<Vec3I>

val Area3I.iterable3I: Iterable<Vec3I>
    get() {
        return object : Iterable<Vec3I> {
            override fun iterator(): Iterator<Vec3I> {
                return iterator {
                    repeat(second.y - first.y) { dy ->
                        repeat(second.z - first.z) { dz ->
                            repeat(second.x - first.x) { dx ->
                                yield(first + Vec3I(dx, dy, dz))
                            }
                        }
                    }
                }
            }
        }
    }

val Area3I.volume3I get() = (second - first).run { x * y * z }

@Serializable
data class Vec3D(
    val x: Double,
    val y: Double,
    val z: Double,
) : Vector<Double, Vec3D> {
    constructor(x: Float, y: Float, z: Float) : this(x.double, y.double, z.double)

    override operator fun plus(vec: Vec3D) = Vec3D(x + vec.x, y + vec.y, z + vec.z)

    override operator fun minus(vec: Vec3D) = Vec3D(x - vec.x, y - vec.y, z - vec.z)

    override operator fun times(value: Double) = Vec3D(x * value, y * value, z * value)

    override operator fun times(vec: Vec3D) = x * vec.x + y * vec.y + z * vec.z

    operator fun div(value: Double) = Vec3D(x / value, y / value, z / value)

    override operator fun unaryPlus() = this

    override operator fun unaryMinus() = Vec3D(-x, -y, -z)

    fun plus(
        dX: Double = 0.0,
        dY: Double = 0.0,
        dZ: Double = 0.0,
    ) = Vec3D(x + dX, y + dY, z + dZ)

    fun times(
        nX: Double = 1.0,
        nY: Double = 1.0,
        nZ: Double = 1.0,
    ) = Vec3D(x * nX, y * nY, z * nZ)

    fun div(
        nX: Double = 1.0,
        nY: Double = 1.0,
        nZ: Double = 1.0,
    ) = Vec3D(x / nX, y / nY, z / nZ)

    fun cross(vec: Vec3D) = Vec3D(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x)

    override val lengthSquared get() = x * x + y * y + z * z

    override val length get() = sqrt(lengthSquared.double)

    override fun min(other: Vec3D) = Vec3D(minOf(x, other.x), minOf(y, other.y), minOf(z, other.z))

    override fun max(other: Vec3D) = Vec3D(maxOf(x, other.x), maxOf(y, other.y), maxOf(z, other.z))

    override fun allLess(other: Vec3D) = x < other.x && y < other.y && z < other.z

    override fun allLessEqual(other: Vec3D) = x <= other.x && y <= other.y && z <= other.z
}

inline val Vec3D.asVec3I get() = Vec3I(x.int, y.int, z.int)

inline val Vec3D.asFloorVec3I get() = Vec3I(x.floorInt, y.floorInt, z.floorInt)

inline val Vec3D.asCeilVec3I get() = Vec3I(x.ceilInt, y.ceilInt, z.ceilInt)

typealias Area3D = Area<Vec3D>
