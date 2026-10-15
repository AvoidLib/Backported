package pl.olafcio.backported.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.ClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import pl.olafcio.backported.BackportedClickEvents;

@Mixin(ClickEvent.Action.class)
public enum ClickEventActionMixin {
    BACKPORTED_CUSTOM("custom", true, BackportedClickEvents.Custom.CODEC);

    @Shadow
    ClickEventActionMixin(String custom, boolean b, MapCodec<BackportedClickEvents.Custom> codec) {
    }
}
