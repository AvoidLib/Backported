package pl.olafcio.backported.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.backported.BackportedInsideBlockEffectApplier;
import pl.olafcio.backported.BackportedInsideBlockEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Level level();

    @Unique
    private static final class State {
        Consumer<Entity> before;
        Consumer<Entity> after;
    }

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"), method = "checkInsideBlocks")
    public void fluidStateHandling(
            List<Entity.Movement> list, Set<BlockState> set, CallbackInfo ci,
            @Local BlockState blockState,
            @Local BlockPos blockPos
    ) {
        var entity = (Entity) (Object) this;
        var applier = new BackportedInsideBlockEffectApplier() {
            private final HashMap<BackportedInsideBlockEffectType, State> states
                    = new HashMap<>();

            @Override
            public void perform(BackportedInsideBlockEffectType effectType) {
                states.put(effectType, new State());
            }

            @Override
            public void performBefore(BackportedInsideBlockEffectType effectType, Consumer<Entity> action) {
                states.get(effectType).before = action;
            }

            @Override
            public void performAfter(BackportedInsideBlockEffectType effectType, Consumer<Entity> action) {
                states.get(effectType).after = action;
            }
        };

        blockState.getFluidState().backported_attr$entityInside(this.level(), blockPos, entity, applier);

        for (var entry : applier.states.entrySet()) {
            var state = entry.getValue();
            if (state.before != null)
                state.before.accept(entity);

            entry.getKey().apply(entity);

            if (state.after != null)
                state.after.accept(entity);
        }
    }
}
