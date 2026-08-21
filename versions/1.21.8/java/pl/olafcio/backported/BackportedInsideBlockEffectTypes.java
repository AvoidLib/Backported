package pl.olafcio.backported;

import net.minecraft.world.entity.InsideBlockEffectType;
import pl.olafcio.backported.mixin.InsideBlockEffectTypeMixin;

public final class BackportedInsideBlockEffectTypes {
    private BackportedInsideBlockEffectTypes() {}

    public static final InsideBlockEffectType CLEAR_FREEZE = (InsideBlockEffectType) (Object) InsideBlockEffectTypeMixin.BACKPORTED_CLEAR_FREEZE;
}
