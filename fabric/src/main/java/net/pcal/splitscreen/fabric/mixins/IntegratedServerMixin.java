package net.pcal.splitscreen.fabric.mixins;

import net.minecraft.server.MinecraftServer;
import net.pcal.splitscreen.fabric.LocalPositionSync;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.pcal.splitscreen.common.logging.SystemLogger.syslog;

@Mixin(net.minecraft.client.server.IntegratedServer.class)
public class IntegratedServerMixin {

    @Inject(method = "publishServer", at = @At("RETURN"))
    private void onPublishServer(MinecraftServer.MultiplayerScope scope, boolean cheatsAllowed, int port, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            syslog().info("LAN server published on port: " + port);
            LocalPositionSync.broadcastLanServer(port);
        }
    }
}
