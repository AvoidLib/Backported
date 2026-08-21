package pl.olafcio.backported.mixininterface;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

public interface IServerboundInteractPacket {
    boolean backported_attr$isWithinRange(ServerPlayer serverPlayer, AABB aABB, double d);
}
