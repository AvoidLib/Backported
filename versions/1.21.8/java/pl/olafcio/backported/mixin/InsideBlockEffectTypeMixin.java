package pl.olafcio.backported.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(InsideBlockEffectType.class)
public enum InsideBlockEffectTypeMixin {
    BACKPORTED_CLEAR_FREEZE((entity) -> {
        entity.setIsInPowderSnow(false);

        if (entity.canFreeze()) {
            entity.setTicksFrozen(Math.max(0, entity.getTicksFrozen() - 1));
        }
    });

    @Shadow
    InsideBlockEffectTypeMixin(Consumer<Entity> consumer) {
    }
}
