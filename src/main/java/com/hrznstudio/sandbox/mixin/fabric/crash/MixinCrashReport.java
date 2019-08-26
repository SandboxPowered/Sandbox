package com.hrznstudio.sandbox.mixin.fabric.crash;

import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public abstract class MixinCrashReport {

    @Shadow
    public abstract CrashReportSection getSystemDetailsSection();

    @Inject(at = @At("RETURN"), method = "fillSystemDetails")
    private void fillSystemDetails(CallbackInfo info) {
        getSystemDetailsSection().add("Sandbox Addons", () -> {
            StringBuilder builder = new StringBuilder();
            builder.append("Sandbox@1.0.0");
            return builder.toString();
        });
    }
}