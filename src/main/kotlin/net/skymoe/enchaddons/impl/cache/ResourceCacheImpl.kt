package net.skymoe.enchaddons.impl.cache

import net.skymoe.enchaddons.impl.MOD_ID
import kotlin.collections.HashMap

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
