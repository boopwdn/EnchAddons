package net.skymoe.enchaddons.impl.mixin;

import cc.polyfrost.oneconfig.config.elements.BasicOption;
import net.skymoe.enchaddons.impl.config.ConfigImplKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(value = BasicOption.class, remap = false)
public abstract class BasicOptionMixin {
    @Unique
    private static final Field categoryField;

    @Unique
    private static final Field subcategoryField;

    static {
        try {
            categoryField = BasicOption.class.getDeclaredField("category");
            categoryField.setAccessible(true);

            subcategoryField = BasicOption.class.getDeclaredField("subcategory");
            subcategoryField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setOverrideCategory(Field field, Object parent, String name, String description, String category, String subcategory, int size, CallbackInfo ci) throws IllegalAccessException {
        List<String> categoryOverride = ConfigImplKt.getCategoryOverride();
        List<String> subCategoryOverride = ConfigImplKt.getSubCategoryOverride();

        if (!categoryOverride.isEmpty()) categoryField.set(this, categoryOverride.get(categoryOverride.size() - 1));
        if (!subCategoryOverride.isEmpty()) subcategoryField.set(this, subCategoryOverride.get(subCategoryOverride.size() - 1));
    }
}
