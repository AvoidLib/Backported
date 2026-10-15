package pl.olafcio.backported.mixininterface;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Vector3f;

public interface IModelPart {
    default void backported_attr$getExtentsForGui(PoseStack poseStack, Set<Vector3f> set) {
        throw new RuntimeException("Implemented via mixin");
    }

    default Function<String, ModelPart> backported_attr$createPartLookup() {
        throw new RuntimeException("Implemented via mixin");
    }
}
