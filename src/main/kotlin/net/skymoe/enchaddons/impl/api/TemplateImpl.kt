package net.skymoe.enchaddons.impl.api

import net.skymoe.enchaddons.api.Template
import net.skymoe.enchaddons.util.scope.noexcept
import org.stringtemplate.v4.*
import java.util.WeakHashMap
import java.util.Date

private val cache = WeakHashMap<String, ST>()

private val group =
    STGroup('<', '>').apply {
        registerRenderer(Number::class.java, NumberRenderer())
        registerRenderer(String::class.java, StringRenderer())
        registerRenderer(Date::class.java, DateRenderer())
    }

class TemplateImpl(
    private val template: String,
) : Template {
    private val st =
        noexcept {
            cache.getOrPut(template) {
                ST(group, template)
            }
        }

    override fun set(
        key: String,
        value: Any?,
    ) {
        st?.add(key, value)
    }

    override fun format() = noexcept { st?.render() } ?: template
}