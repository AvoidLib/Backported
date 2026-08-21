package pl.olafcio.backported.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pl.olafcio.backported.BackportedDataComponents;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
       extends Entity
{
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/core/Holder;)V"), method = {"stopUsingItem", "startUsingItem", "checkTotemDeathProtection"})
    public void vibrato(LivingEntity instance, Holder<GameEvent> holder) {
        causeUseVibration(instance.getUseItem(), holder);
    }

    @Unique
    private void causeUseVibration(ItemStack stack, Holder<GameEvent> reference) {
        var useEffects = stack.get(BackportedDataComponents.USE_EFFECTS);
        if (useEffects != null && useEffects.interactVibrations()) {
            this.gameEvent(reference);
        }
    }
}
