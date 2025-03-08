package net.skymoe.enchaddons.util

import java.nio.ByteBuffer

fun ByteArray.toBuffer(): ByteBuffer =
    ByteBuffer.allocateDirect(this.size).apply {
        put(this@toBuffer)
        flip()
    }
