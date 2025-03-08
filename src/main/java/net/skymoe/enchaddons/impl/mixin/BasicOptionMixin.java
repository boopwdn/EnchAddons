package net.skymoe.enchaddons.impl.mixin;

import cc.polyfrost.oneconfig.config.elements.BasicOption;
import net.skymoe.enchaddons.impl.config.adapter.Extract;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(BasicOption.class)
public class BasicOptionMixin {
    @Mutable
    @Shadow @Final public String category;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initBasicOption(Field field, Object parent, String name, String description, String category, String subcategory, int size, CallbackInfo ci) {
        Extract extract = field.getAnnotation(Extract.class);
        if (extract != null) {
            this.category = extract.category();
        }
    }
}
