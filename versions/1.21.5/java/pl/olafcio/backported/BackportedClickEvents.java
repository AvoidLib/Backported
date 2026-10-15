package pl.olafcio.backported;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.resources.ResourceLocation;

import pl.olafcio.backported.mixin.ClickEventActionMixin;

public final class BackportedClickEvents {
    private BackportedClickEvents() {}

    public record Custom(ResourceLocation id, Optional<Tag> payload) implements ClickEvent {
        public static final MapCodec<BackportedClickEvents.Custom> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                ResourceLocation.CODEC.fieldOf("id").forGetter(BackportedClickEvents.Custom::id),
                                BackportedExtraCodecs.NBT.optionalFieldOf("payload").forGetter(BackportedClickEvents.Custom::payload)
                        )
                        .apply(instance, BackportedClickEvents.Custom::new)
        );

        @Override
        public ClickEvent.Action action() {
            return ClickEvent.Action.valueOf("BACKPORTED_CUSTOM");
        }
    }
}
