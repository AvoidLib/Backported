package pl.olafcio.backported;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import pl.olafcio.backported.components.UseEffects;

public final class BackportedDataComponents {
    private BackportedDataComponents() {}

    public static final DataComponentType<UseEffects> USE_EFFECTS =
            Registry.register(
                    BuiltInRegistries.DATA_COMPONENT_TYPE,
                    ResourceLocation.fromNamespaceAndPath("backported", "use_effects"),
                    DataComponentType.<UseEffects>builder()
                                     .persistent(UseEffects.CODEC)
                                     .networkSynchronized(UseEffects.STREAM_CODEC)
                                     .build()
            );

    public static void init() {}
}
