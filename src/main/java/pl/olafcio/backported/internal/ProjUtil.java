package pl.olafcio.backported.internal;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import pl.olafcio.backported.components.AttackRange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ProjUtil {
    public static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(
            Entity entity, AttackRange attackRange, Predicate<Entity> predicate, ClipContext.Block block
    ) {
        Vec3 vec3 = entity.getLookAngle();
        Vec3 vec32 = entity.getEyePosition();
        Vec3 vec33 = vec32.add(vec3.scale(attackRange.effectiveMinRange(entity)));
        double d = entity.getKnownMovement().dot(vec3);
        Vec3 vec34 = vec32.add(vec3.scale(attackRange.effectiveMaxRange(entity) + Math.max(0.0, d)));
        return getHitEntitiesAlong(entity, vec32, vec33, predicate, vec34, attackRange.hitboxMargin(), block);
    }

    private static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(
            Entity entity, Vec3 vec3, Vec3 vec32, Predicate<Entity> predicate, Vec3 vec33, float f, ClipContext.Block block
    ) {
        Level level = entity.level();
        BlockHitResult blockHitResult = level.clipIncludingBorder(new ClipContext(vec3, vec33, block, ClipContext.Fluid.NONE, entity));
        if (blockHitResult.getType() != HitResult.Type.MISS) {
            vec33 = blockHitResult.getLocation();
            if (vec3.distanceToSqr(vec33) < vec3.distanceToSqr(vec32)) {
                return Either.left(blockHitResult);
            }
        }

        AABB aABB = AABB.ofSize(vec32, f, f, f).expandTowards(vec33.subtract(vec32)).inflate(1.0);
        Collection<EntityHitResult> collection = getManyEntityHitResult(level, entity, vec32, vec33, aABB, predicate, f, block, true);
        return !collection.isEmpty() ? Either.right(collection) : Either.left(blockHitResult);
    }

    public static Collection<EntityHitResult> getManyEntityHitResult(
            Level level, Entity entity, Vec3 vec3, Vec3 vec32, AABB aABB, Predicate<Entity> predicate, float f, ClipContext.Block block, boolean bl
    ) {
        List<EntityHitResult> list = new ArrayList();

        for (Entity entity2 : level.getEntities(entity, aABB, predicate)) {
            AABB aABB2 = entity2.getBoundingBox();
            if (bl && aABB2.contains(vec3)) {
                list.add(new EntityHitResult(entity2, vec3));
            } else {
                Optional<Vec3> optional = aABB2.clip(vec3, vec32);
                if (optional.isPresent()) {
                    list.add(new EntityHitResult(entity2, (Vec3)optional.get()));
                } else if (!(f <= 0.0)) {
                    Optional<Vec3> optional2 = aABB2.inflate(f).clip(vec3, vec32);
                    if (!optional2.isEmpty()) {
                        Vec3 vec33 = (Vec3)optional2.get();
                        Vec3 vec34 = aABB2.getCenter();
                        BlockHitResult blockHitResult = level.clipIncludingBorder(new ClipContext(vec33, vec34, block, ClipContext.Fluid.NONE, entity));
                        if (blockHitResult.getType() != HitResult.Type.MISS) {
                            vec34 = blockHitResult.getLocation();
                        }

                        Optional<Vec3> optional3 = entity2.getBoundingBox().clip(vec33, vec34);
                        if (optional3.isPresent()) {
                            list.add(new EntityHitResult(entity2, (Vec3)optional3.get()));
                        }
                    }
                }
            }
        }

        return list;
    }
}
