package net.skymoe.enchaddons.impl.oneconfig

import cc.polyfrost.oneconfig.renderer.font.Font
import net.skymoe.enchaddons.util.property.latelet

var nvg: NanoVGAccessor by latelet()

private var fontLoaded = false

var fontMediumInstance: Font by latelet()
var fontSemiBoldInstance: Font by latelet()

val fontMedium = { fontMediumInstance }
val fontSemiBold = { fontSemiBoldInstance }

fun NanoVGAccessor.loadFonts(vg: Long) {
    if (!fontLoaded) {
        fontMediumInstance = loadFont(vg, "montserrat/medium.ttf")
        fontSemiBoldInstance = loadFont(vg, "montserrat/semibold.ttf")
        fontLoaded = true
    }
}

class NanoVGImageCache {
    private val cache = mutableMapOf<Any?, MutableMap<Int, Int>>()
    private val imagesToDelete = mutableSetOf<Int>()

    operator fun get(key: Any?) = NanoVGImageCacheEntry(this, cache.getOrPut(key, ::mutableMapOf))

    fun cleanup(
        accessor: NanoVGAccessor,
        vg: Long,
    ) {
        accessor.deleteImages(vg, imagesToDelete)
        imagesToDelete.clear()
    }

    fun clear() {
        cache.values.forEach { entry ->
            entry.values.forEach(imagesToDelete::add)
        }
        cache.clear()
    }

    fun clear(key: Any?) {
        cache[key]?.values?.forEach(imagesToDelete::add)
        cache.remove(key)
    }
}

class NanoVGImageCacheEntry(
    val parent: NanoVGImageCache,
    val cache: MutableMap<Int, Int>,
) {
    fun cleanup(
        accessor: NanoVGAccessor,
        vg: Long,
    ) {
        parent.cleanup(accessor, vg)
    }
}

interface NanoVGAccessor {
    fun loadFont(
        vg: Long,
        name: String,
    ): Font

    fun deleteImages(
        vg: Long,
        images: Set<Int>,
    )

    fun drawRingArc(
        vg: Long,
        x: Double,
        y: Double,
        outerRadius: Double,
        innerRadius: Double,
        fromRadian: Double,
        toRadian: Double,
        arcPaddingFrom: Double,
        arcPaddingTo: Double,
        color: Int,
    )

    fun drawRoundedPlayerAvatar(
        vg: Long,
        imageCache: NanoVGImageCacheEntry,
        texture: Int,
        hat: Boolean,
        scaleHat: Boolean,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        alpha: Double,
        radius: Double,
    )
}
