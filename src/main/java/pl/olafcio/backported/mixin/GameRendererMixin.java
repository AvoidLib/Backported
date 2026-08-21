package pl.olafcio.backported.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import pl.olafcio.backported.BackportedDataComponents;
import pl.olafcio.backported.mixininterface.ILivingEntity;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    private static HitResult filterHitResult(HitResult hitResult, Vec3 vec3, double d) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;"), method = "pick(F)V")
    public HitResult pick__pick(GameRenderer instance, Entity entity, double blockInteractionRange, double entityInteractionRange, float f, Operation<HitResult> original) {
        var self = this.minecraft.player;

        var itemStack = ((ILivingEntity) self).backported_attr$getActiveItem();
        var attackRange = itemStack.get(BackportedDataComponents.ATTACK_RANGE);

        HitResult hitResult = null;

        if (attackRange != null) {
            hitResult = attackRange.getClosesetHit(entity, f, EntitySelector.CAN_BE_PICKED);

            if (hitResult instanceof BlockHitResult) {
                hitResult = filterHitResult(hitResult, entity.getEyePosition(f), blockInteractionRange);
            }
        }

        if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) {
            hitResult = original.call(instance, entity, blockInteractionRange, entityInteractionRange, f);
        }

        return hitResult;
    }
}
