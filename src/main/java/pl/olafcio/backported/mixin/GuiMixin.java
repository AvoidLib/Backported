package pl.olafcio.backported.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import pl.olafcio.backported.BackportedDataComponents;
import pl.olafcio.backported.mixininterface.ILivingEntity;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isAlive()Z"), method = "renderCrosshair")
    public boolean renderCrosshair__isAlive(Entity instance, Operation<Boolean> original) {
        if (!original.call(instance))
            return false;

        var attackRange = ((ILivingEntity) this.minecraft.player).backported_attr$getActiveItem().get(BackportedDataComponents.ATTACK_RANGE);
        return attackRange == null || attackRange.isInRange(this.minecraft.player, this.minecraft.hitResult.getLocation());
    }
}
