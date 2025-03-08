package net.skymoe.enchaddons.impl.mixin;

import cc.polyfrost.oneconfig.config.elements.BasicOption;
import net.skymoe.enchaddons.impl.config.ConfigImplKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(value = BasicOption.class, remap = false)
public class BasicOptionMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 4, argsOnly = true)
    private String initBasicOptionCategory(String category) {
        List<String> categoryOverride = ConfigImplKt.getCategoryOverride();

        if (!categoryOverride.isEmpty()) return categoryOverride.get(categoryOverride.size() - 1);
        return category;
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 5, argsOnly = true)
    private String initBasicOptionSubCategory(String subCategory) {
        List<String> subCategoryOverride = ConfigImplKt.getSubCategoryOverride();

        if (!subCategoryOverride.isEmpty()) return subCategoryOverride.get(subCategoryOverride.size() - 1);
        return subCategory;
    }
}
