package net.skymoe.enchaddons.impl.cache

import net.skymoe.enchaddons.impl.MOD_ID
import net.skymoe.enchaddons.impl.oneconfig.nvg
import kotlin.collections.HashMap

fun getImageLoader(path: String): (Long) -> Int {
    var image: Int? = null
    return { vg ->
        (image ?: nvg.loadImageFromByteArray(vg, ResourceCacheImpl.get(path))).also { image = it }
    }
}

object ResourceCacheImpl {
    private val cacheMap: HashMap<String, ByteArray> = hashMapOf()

    fun get(
        resourcePath: String,
        reload: Boolean = false,
    ): ByteArray =
        if (reload) {
            javaClass.getResource("/assets/$MOD_ID/$resourcePath")!!.readBytes().also {
                cacheMap[resourcePath] = it
            }
        } else {
            cacheMap
                .getOrPut(resourcePath) {
                    javaClass.getResource("/assets/$MOD_ID/$resourcePath")!!.readBytes()
                }
        }
}
