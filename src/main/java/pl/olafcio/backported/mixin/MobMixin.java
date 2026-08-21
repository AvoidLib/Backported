package pl.olafcio.backported.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.*;
import pl.olafcio.backported.BackportedDataComponents;
import pl.olafcio.backported.mixininterface.ILivingEntity;

@Mixin(Mob.class)
public abstract class MobMixin
       extends Entity
       implements ILivingEntity
{
    @Shadow
    @Final
    private static double DEFAULT_ATTACK_REACH;

    public MobMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author Backported
     * @reason
     */
    @Overwrite
    public boolean isWithinMeleeAttackRange(LivingEntity livingEntity) {
        var attackRange = this.backported_attr$getActiveItem().get(BackportedDataComponents.ATTACK_RANGE);

        double max;
        double min;

        if (attackRange == null) {
            max = DEFAULT_ATTACK_REACH;
            min = 0.0;
        } else {
            max = attackRange.effectiveMaxRange(this);
            min = attackRange.effectiveMinRange(this);
        }

        var aABB = livingEntity.getHitbox();
        return this.getAttackBoundingBox(max).intersects(aABB) && (min <= 0.0 || !this.getAttackBoundingBox(min).intersects(aABB));
    }

    @Unique
    protected AABB getAttackBoundingBox(double d) {
        Entity entity = this.getVehicle();
        AABB aABB3;
        if (entity != null) {
            AABB aABB = entity.getBoundingBox();
            AABB aABB2 = this.getBoundingBox();
            aABB3 = new AABB(
                    Math.min(aABB2.minX, aABB.minX), aABB2.minY, Math.min(aABB2.minZ, aABB.minZ), Math.max(aABB2.maxX, aABB.maxX), aABB2.maxY, Math.max(aABB2.maxZ, aABB.maxZ)
            );
        } else {
            aABB3 = this.getBoundingBox();
        }

        return aABB3.inflate(d, 0.0, d);
    }
}
