package org.sandboxpowered.sandbox.fabric.mixin.fabric.crash;

import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.sandboxpowered.sandbox.fabric.client.SandboxClient;
import org.sandboxpowered.sandbox.fabric.server.SandboxServer;
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
            if (SandboxServer.INSTANCE != null) {
                builder.append("Server: \n");
                SandboxServer.INSTANCE.loader.getAddons().forEach(spec -> {
                    builder.append("- ").append(spec.getModid());

                    if (!spec.getTitle().equals(spec.getModid())) {
                        builder.append(" \"").append(spec.getTitle()).append("\"");
                    }

                    builder.append(" @ ").append(spec.getVersion().toString()).append("\n");
                });
            }

            if (SandboxClient.INSTANCE != null) {
                builder.append("Client: \n");
                SandboxClient.INSTANCE.loader.getAddons().forEach(spec -> {
                    builder.append("- ").append(spec.getModid());

                    if (!spec.getTitle().equals(spec.getModid())) {
                        builder.append(" \"").append(spec.getTitle()).append("\"");
                    }

                    builder.append(" @ ").append(spec.getVersion().toString()).append("\n");
                });
            }

            return builder.toString();
        });
    }
}