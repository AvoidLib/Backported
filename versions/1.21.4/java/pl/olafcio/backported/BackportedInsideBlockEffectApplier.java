package pl.olafcio.backported;

import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public interface BackportedInsideBlockEffectApplier {
    void perform(BackportedInsideBlockEffectType var1);
    void performBefore(BackportedInsideBlockEffectType var1, Consumer<Entity> var2);
    void performAfter(BackportedInsideBlockEffectType var1, Consumer<Entity> var2);
}
