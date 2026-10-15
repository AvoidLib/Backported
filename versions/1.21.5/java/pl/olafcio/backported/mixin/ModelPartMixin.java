package pl.olafcio.backported.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import pl.olafcio.backported.mixininterface.IModelPart;
import net.minecraft.client.model.geom.ModelPart;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin implements IModelPart {
    @Shadow
    public abstract void visit(PoseStack poseStack, ModelPart.Visitor visitor);

    @Shadow
    @Final
    private Map<String, ModelPart> children;

    @Override
    public void backported_attr$getExtentsForGui(PoseStack poseStack, Set<Vector3f> set) {
        this.visit(poseStack, (pose, string, i, cube) -> {
            for (ModelPart.Polygon polygon : cube.polygons) {
                for (ModelPart.Vertex vertex : polygon.vertices()) {
                    float f = vertex.pos().x() / 16.0F;
                    float g = vertex.pos().y() / 16.0F;
                    float h = vertex.pos().z() / 16.0F;
                    Vector3f vector3f = pose.pose().transformPosition(f, g, h, new Vector3f());
                    set.add(vector3f);
                }
            }
        });
    }

    @Override
    public Function<String, ModelPart> backported_attr$createPartLookup() {
        Map<String, ModelPart> map = new HashMap<>();
        map.put("root", (ModelPart) (Object) this);
        this.addAllChildren(map::putIfAbsent);
        return map::get;
    }

    @Unique
    private void addAllChildren(BiConsumer<String, ModelPart> biConsumer) {
        for (Map.Entry<String, ModelPart> entry : this.children.entrySet()) {
            biConsumer.accept((String)entry.getKey(), (ModelPart)entry.getValue());
        }

        for (ModelPart modelPart : this.children.values()) {
            ((ModelPartMixin) (Object) modelPart).addAllChildren(biConsumer);
        }
    }
}
