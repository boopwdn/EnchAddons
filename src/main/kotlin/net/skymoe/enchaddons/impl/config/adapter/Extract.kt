package net.skymoe.enchaddons.impl.config.adapter

import cc.polyfrost.oneconfig.config.annotations.CustomOption

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@CustomOption(id = "extract")
annotation class Extract