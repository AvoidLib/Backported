package pl.olafcio.backported.mixin;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pl.olafcio.backported.mixininterface.IPlayer;
import pl.olafcio.backported.mixininterface.IServerboundInteractPacket;

@Mixin(ServerboundInteractPacket.class)
public class ServerboundInteractPacketMixin implements IServerboundInteractPacket {
    @Shadow
    @Final
    private ServerboundInteractPacket.Action action;

    @Override
    public boolean backported_attr$isWithinRange(ServerPlayer serverPlayer, AABB aABB, double d) {
        return this.action.getType() == ServerboundInteractPacket.ActionType.ATTACK
                ? ((IPlayer) serverPlayer).backported_attr$isWithinAttackRange(aABB, d)
                : serverPlayer.canInteractWithEntity(aABB, d);
    }
}
