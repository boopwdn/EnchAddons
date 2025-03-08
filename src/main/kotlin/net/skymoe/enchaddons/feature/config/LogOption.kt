
package net.skymoe.enchaddons.feature.config

import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.api.Template
import net.skymoe.enchaddons.api.format
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger

interface LogOption {
    val enabled: Boolean
    val level: Int
    val text: String
}

inline operator fun LogOption.invoke(
    logger: Logger,
    placeholder: Template.() -> Unit,
) {
    if (enabled) {
        logger.log(
            when (level) {
                0 -> Level.FATAL
                1 -> Level.ERROR
                2 -> Level.WARN
                3 -> Level.INFO
                4 -> Level.DEBUG
                else -> Level.TRACE
            },
            EA.api.format(text, placeholder),
        )
    }
}
