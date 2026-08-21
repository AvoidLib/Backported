package pl.olafcio.backported.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pl.olafcio.backported.BackportedDataComponents;
import pl.olafcio.backported.components.UseEffects;

@Mixin(DataComponents.class)
public class DataComponentsMixin {
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/component/DataComponentMap$Builder;build()Lnet/minecraft/core/component/DataComponentMap;"), method = "<clinit>")
    private static DataComponentMap build(DataComponentMap.Builder instance, Operation<DataComponentMap> original) {
        return original.call(instance
                       .set(BackportedDataComponents.USE_EFFECTS, UseEffects.DEFAULT));
    }
}
