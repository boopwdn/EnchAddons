package net.skymoe.enchaddons.util

import java.io.PrintWriter
import java.io.StringWriter

val Throwable.stackTraceMessage: String
    get() {
        return StringWriter()
            .also { sw ->
                PrintWriter(sw).use { pw ->
                    printStackTrace(pw)
                    pw.flush()
                }
            }.toString()
    }