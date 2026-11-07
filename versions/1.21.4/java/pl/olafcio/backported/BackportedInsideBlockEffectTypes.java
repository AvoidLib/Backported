package pl.olafcio.backported;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public final class BackportedInsideBlockEffectTypes {
    private BackportedInsideBlockEffectTypes() {}

    public static final BackportedInsideBlockEffectType FREEZE = entity -> {
        entity.setIsInPowderSnow(true);

        if (entity.canFreeze()) {
            entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze(), entity.getTicksFrozen() + 1));
        }
    };

    public static final BackportedInsideBlockEffectType CLEAR_FREEZE = entity -> {
        entity.setTicksFrozen(0);
    };

    public static final BackportedInsideBlockEffectType FIRE_IGNITE = entity -> {
        if (!entity.fireImmune()) {
            if (entity.getRemainingFireTicks() < 0) {
                entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            } else if (entity instanceof ServerPlayer) {
                int i = entity.level().getRandom().nextInt(1, 3);
                entity.setRemainingFireTicks(entity.getRemainingFireTicks() + i);
            }

            if (entity.getRemainingFireTicks() >= 0) {
                entity.igniteForSeconds(8.0f);
            }
        }
    };

    public static final BackportedInsideBlockEffectType LAVA_IGNITE = entity -> {
        if (entity.fireImmune()) {
            return;
        }

        entity.igniteForSeconds(15.0f);
    };

    public static final BackportedInsideBlockEffectType EXTINGUISH = Entity::clearFire;
}
