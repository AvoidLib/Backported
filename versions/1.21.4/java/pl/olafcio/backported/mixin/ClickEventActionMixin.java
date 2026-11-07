package pl.olafcio.backported.mixin;

import net.minecraft.network.chat.ClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClickEvent.Action.class)
public enum ClickEventActionMixin {
    BACKPORTED_CUSTOM("custom", true);

    @Shadow
    ClickEventActionMixin(String custom, boolean b) {
    }
}
