package pl.olafcio.backported.mixin;

import com.google.common.io.ByteStreams;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import pl.olafcio.backported.BackportedClickEvents;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Mixin(Style.class)
public class StyleMixin {
    @WrapMethod(method = "create")
    private static Style create(
            Optional<TextColor> textColor, Optional<Integer> shadowColor, Optional<Boolean> bold,
            Optional<Boolean> italic, Optional<Boolean> underlined, Optional<Boolean> strikethrough,
            Optional<Boolean> obfuscated, Optional<ClickEvent> clickEvent, Optional<HoverEvent> hoverEvent,
            Optional<String> insertion, Optional<ResourceLocation> font, Operation<Style> original
    ) {
        return original.call(textColor, shadowColor, bold, italic, underlined, strikethrough, obfuscated, clickEvent.map(value -> {
            if (value.getAction() == BackportedClickEvents.CUSTOM) {
                try {
                    var contents = CompoundTag.TYPE.load(
                            ByteStreams.newDataInput(value.getValue().getBytes(StandardCharsets.UTF_8)),
                            NbtAccounter.create(2000L)
                    );

                    return new BackportedClickEvents.Custom(
                            ResourceLocation.parse(contents.getString("id")),
                            Optional.ofNullable(contents.get("payload"))
                    );
                } catch (IOException ignored) {
                    // Don't want to crash the server or smth, so let it go!
                }
            }

            return value;
        }), hoverEvent, insertion, font);
    }
}
