package net.skymoe.enchaddons.impl.mixin;

import cc.polyfrost.oneconfig.internal.renderer.LwjglManagerImpl;
import net.skymoe.enchaddons.EnchAddonsKt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;

@Mixin(LwjglManagerImpl.class)
public class LwjglManagerImplMixin extends URLClassLoader {
    @Unique
    private static final String enchAddons$PACKAGE = "net.skymoe.enchaddons.impl.oneconfig.ciallo.";

    @Shadow(remap = false)
    @Final
    private Set<String> classLoaderInclude;

    @Shadow(remap = false)
    @Final
    private Map<String, Class<?>> classCache;

    public LwjglManagerImplMixin(URL[] urls) {
        super(urls);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initPost(CallbackInfo ci) throws Exception {
        addURL(EnchAddonsKt.getCLASS_ROOT());
        classLoaderInclude.add(enchAddons$PACKAGE);
        Class.forName(enchAddons$PACKAGE + "NanoVGAccessorImpl", true, this);
    }

    @Inject(method = "findClass", at = @At("HEAD"), remap = false, cancellable = true)
    private void findClassPre(String name, CallbackInfoReturnable<Class<?>> cir) throws Exception {
        if (name.startsWith(enchAddons$PACKAGE)) {
            Class<?> clazz;
            if (classCache.containsKey(name)) {
                clazz = classCache.get(name);
            } else {
                clazz = super.findClass(name);
                classCache.put(name, clazz);
            }
            cir.setReturnValue(clazz);
        }
    }
}
