package pl.olafcio.backported.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import pl.olafcio.backported.BackportedDataComponents;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Nullable
    public HitResult hitResult;

    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;

    @Shadow
    @Nullable
    public LocalPlayer player;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;attack(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)V"), method = "startAttack")
    public void startAttack__attack(MultiPlayerGameMode instance, Player player, Entity entity, Operation<Void> original) {
        var itemStack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
        var attackRange = itemStack.get(BackportedDataComponents.ATTACK_RANGE);

        if (attackRange == null || attackRange.isInRange(player, this.hitResult.getLocation())) {
            this.gameMode.attack(this.player, ((EntityHitResult)this.hitResult).getEntity());
        }
    }
}
