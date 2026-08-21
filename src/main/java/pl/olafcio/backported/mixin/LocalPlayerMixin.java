package pl.olafcio.backported.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.olafcio.backported.BackportedDataComponents;
import pl.olafcio.backported.components.UseEffects;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin
       extends LivingEntity
{
    protected LocalPlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract boolean isUsingItem();

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"), method = {"aiStep", "canStartSprinting"})
    public boolean aiStep__isUsingItem(LocalPlayer instance) {
        return isSlowDueToUsingItem();
    }

    @Unique
    private boolean isSlowDueToUsingItem() {
        return this.isUsingItem() && !this.useItem.getOrDefault(BackportedDataComponents.USE_EFFECTS, UseEffects.DEFAULT).canSprint();
    }

    @ModifyConstant(constant = @Constant(floatValue = 0.2F), method = "modifyInput")
    private float modifyInput__itemusespeedmul(float constant) {
        return itemUseSpeedMultiplier();
    }

    @Unique
    private float itemUseSpeedMultiplier() {
        return this.useItem.getOrDefault(BackportedDataComponents.USE_EFFECTS, UseEffects.DEFAULT).speedMultiplier();
    }
}
