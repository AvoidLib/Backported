package pl.olafcio.backported.mixininterface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import pl.olafcio.backported.BackportedInsideBlockEffectApplier;

public interface IFluid {
    default void backported_attr$entityInside(Level level, BlockPos blockPos, Entity entity, BackportedInsideBlockEffectApplier applier) {
    }
}
