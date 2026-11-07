package pl.olafcio.backported;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.resources.ResourceLocation;

public final class BackportedClickEvents {
    public static final ClickEvent.Action CUSTOM = ClickEvent.Action.valueOf("BACKPORTED_CUSTOM");

    private BackportedClickEvents() {}

    public static class Custom extends ClickEvent {
        private final ResourceLocation id;
        private final Optional<Tag> payload;

        public ResourceLocation id() {
            return id;
        }

        public Optional<Tag> payload() {
            return payload;
        }

        public static final MapCodec<BackportedClickEvents.Custom> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                        ResourceLocation.CODEC.fieldOf("id").forGetter(BackportedClickEvents.Custom::id),
                                        BackportedExtraCodecs.NBT.optionalFieldOf("payload").forGetter(BackportedClickEvents.Custom::payload)
                                )
                                .apply(instance, Custom::new)
        );

        public Custom(ResourceLocation id, Optional<Tag> payload) {
            super(CUSTOM, generateValue(id, payload));
            this.id = id;
            this.payload = payload;
        }

        private static String generateValue(ResourceLocation id, Optional<Tag> payload) {
            var tag = new CompoundTag();

            tag.putString("id", id.toString());

            //noinspection OptionalIsPresent
            if (payload.isPresent())
                tag.put("payload", payload.get());

            return tag.toString();
        }
    }
}
