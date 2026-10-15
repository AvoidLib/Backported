package pl.olafcio.backported;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

import static net.minecraft.util.ExtraCodecs.converter;

public class BackportedExtraCodecs {
    public static final Codec<Tag> NBT = converter(NbtOps.INSTANCE);
}
