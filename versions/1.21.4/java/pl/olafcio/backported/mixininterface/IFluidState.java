package pl.olafcio.backported.mixininterface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import pl.olafcio.backported.BackportedInsideBlockEffectApplier;

public interface IFluidState {
    default void backported_attr$entityInside(Level level, BlockPos blockPos, Entity entity, BackportedInsideBlockEffectApplier applier) {
        ((FluidState) (Object) this).getType().backported_attr$entityInside(level, blockPos, entity, applier);
    }
}
