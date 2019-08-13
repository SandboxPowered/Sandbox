package com.hrznstudio.sandbox.mixin.networking;

import com.hrznstudio.sandbox.SandboxConfig;
import com.hrznstudio.sandbox.api.ClientConnectionInternal;
import com.hrznstudio.sandbox.api.CustomPayloadPacket;
import com.hrznstudio.sandbox.util.VelocityUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.packet.LoginQueryRequestS2CPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.packet.LoginQueryResponseC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(ServerLoginNetworkHandler.class)
public abstract class MixinServerLoginNetworkHandler {

    @Shadow
    @Final
    public ClientConnection client;
    @Shadow
    private GameProfile profile;
    private int velocityId = -1;

    @Shadow
    public abstract void disconnect(Text text_1);

    @Shadow
    public abstract void acceptPlayer();

    @Inject(method = "onHello", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerLoginNetworkHandler$State;READY_TO_ACCEPT:Lnet/minecraft/server/network/ServerLoginNetworkHandler$State;"), cancellable = true)
    public void onHello(CallbackInfo info) {
        if (SandboxConfig.velocity.get()) {
            this.velocityId = ThreadLocalRandom.current().nextInt();
            LoginQueryRequestS2CPacket packet = VelocityUtil.create(velocityId);
            this.client.send(packet);
            info.cancel();
        }
    }

    @Inject(method = "onQueryResponse", at = @At("HEAD"), cancellable = true)
    public void onQueryResponse(LoginQueryResponseC2SPacket packet, CallbackInfo info) {
        if (SandboxConfig.velocity.get() && velocityId == ((CustomPayloadPacket.LoginQueryPacket) packet).getQueryId()) {
            PacketByteBuf buf = ((CustomPayloadPacket.LoginQueryPacket) packet).getBuffer();
            if (buf == null) {
                this.disconnect(new LiteralText("This server requires you to log in through a proxy"));
                info.cancel();
                return;
            }
            if (!VelocityUtil.checkIntegrity(buf)) {
                this.disconnect(new LiteralText("Unable to verify player details"));
                info.cancel();
                return;
            }

            ((ClientConnectionInternal) this.client).setSocketAddress(new InetSocketAddress(VelocityUtil.readAddress(buf), ((InetSocketAddress) this.client.getAddress()).getPort()));
            this.profile = VelocityUtil.createProfile(buf);
            acceptPlayer();
            info.cancel();
        }
    }
}