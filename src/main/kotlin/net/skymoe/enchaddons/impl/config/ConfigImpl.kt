package net.skymoe.enchaddons.impl.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Button
import cc.polyfrost.oneconfig.config.annotations.CustomOption
import cc.polyfrost.oneconfig.config.core.ConfigUtils
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.config.elements.OptionPage
import cc.polyfrost.oneconfig.config.elements.SubConfig
import cc.polyfrost.oneconfig.config.migration.Migrator
import cc.polyfrost.oneconfig.internal.config.annotations.Option
import net.skymoe.enchaddons.feature.FeatureInfo
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.impl.config.adapter.Extract
import java.lang.reflect.Field
import java.lang.reflect.Method

abstract class ConfigImpl(
    info: FeatureInfo<*>,
) : SubConfig(info.name, info.configFile),
    FeatureConfig {
    init {
        super.enabled = false
    }

    override val enabled get() = EnchAddonsConfig.enabled && super.enabled

    override fun getCustomOption(
        field: Field,
        annotation: CustomOption,
        page: OptionPage,
        mod: Mod,
        migrate: Boolean,
    ): BasicOption? {
        field.isAccessible = true
        return handleExtensionOption(this, field, { field[this] }, annotation, page, mod, "${field.name}.")
    }

    open fun postInitialized() {}
}

private val internalAddOptionToPageMethod: Method by lazy {
    ConfigUtils::class.java
        .getDeclaredMethod(
            "addOptionToPage",
            OptionPage::class.java,
            Option::class.java,
            Field::class.java,
            Object::class.java,
            Migrator::class.java,
        ).apply { isAccessible = true }
}

var categoryOverride = mutableListOf<String>()
var subCategoryOverride = mutableListOf<String>()

private fun addOptions(
    config: Config,
    instance: Any,
    type: Class<*>,
    page: OptionPage,
    mod: Mod,
    prefix: String,
    category: String,
    subCategory: String,
) {
    if (category.isNotEmpty()) categoryOverride += category
    if (subCategory.isNotEmpty()) subCategoryOverride += subCategory

    type.superclass
        .takeIf { it !== Object::class.java }
        ?.let { addOptions(config, instance, it, page, mod, prefix, category, subCategory) }

    type.declaredFields.forEach { field ->
        field.isAccessible = true
        val optionName = "$prefix${field.name}"

        ConfigUtils.findAnnotation(field, Option::class.java)?.also { annotation ->
            config.optionNames[optionName] =
                internalAddOptionToPageMethod(null, page, annotation, field, instance, null) as BasicOption
        } ?: ConfigUtils.findAnnotation(field, CustomOption::class.java)?.also { annotation ->
            handleExtensionOption(
                config,
                field,
                { field[instance] },
                annotation,
                page,
                mod,
                "$prefix${field.name}.",
            )?.let { basicOption ->
                config.optionNames[optionName] = basicOption
            }
        }

        type.declaredMethods.forEach { method ->
            method.isAccessible = true
            ConfigUtils.findAnnotation(method, Button::class.java)?.let {
                config.optionNames["$prefix${method.name}"] = ConfigUtils.addOptionToPage(page, method, instance)
            }
        }
    }

    if (category.isNotEmpty()) categoryOverride.removeLast()
    if (subCategory.isNotEmpty()) subCategoryOverride.removeLast()
}

private fun handleExtensionOption(
    config: Config,
    field: Field,
    fieldGetter: () -> Any,
    annotation: CustomOption,
    page: OptionPage,
    mod: Mod,
    prefix: String,
): BasicOption? {
    val args = annotation.id.split(":")
    return when (args[0]) {
        "extract" -> {
            val extract: Extract? = field.getAnnotation(Extract::class.java)
            val category = extract?.category ?: ""
            val subCategory = extract?.subCategory ?: ""
            handleExtractOption(config, fieldGetter, page, mod, prefix, category, subCategory)
        }
        else -> null
    }
}

// usage: @Extract
// recursive extraction is supported
private fun handleExtractOption(
    config: Config,
    fieldGetter: () -> Any,
    page: OptionPage,
    mod: Mod,
    prefix: String,
    category: String,
    subCategory: String,
): BasicOption? {
    val value = fieldGetter()
    addOptions(config, value, value::class.java, page, mod, prefix, category, subCategory)
    return null
}
