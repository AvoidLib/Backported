package pl.olafcio.backported.mixin;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.olafcio.backported.mixininterface.IServerboundInteractPacket;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;canInteractWithEntity(Lnet/minecraft/world/phys/AABB;D)Z"), method = "handleInteract")
    public boolean handleInteract__canInteractWithEntity(ServerPlayer instance, AABB aabb, double v, ServerboundInteractPacket serverboundInteractPacket) {
        return ((IServerboundInteractPacket) serverboundInteractPacket).backported_attr$isWithinRange(instance, aabb, v);
    }
}
