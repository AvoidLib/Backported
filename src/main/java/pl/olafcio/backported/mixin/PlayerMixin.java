package pl.olafcio.backported.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import pl.olafcio.backported.BackportedDataComponents;
import pl.olafcio.backported.components.AttackRange;
import pl.olafcio.backported.mixininterface.ILivingEntity;
import pl.olafcio.backported.mixininterface.IPlayer;

@Mixin(Player.class)
public abstract class PlayerMixin
       extends LivingEntity
       implements ILivingEntity, IPlayer
{
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean backported_attr$isWithinAttackRange(AABB aABB, double d) {
        return this.entityAttackRange().isInRange(this, aABB, d);
    }

    @Unique
    public AttackRange entityAttackRange() {
        var attackRange = this.backported_attr$getActiveItem().get(BackportedDataComponents.ATTACK_RANGE);
        return attackRange != null ? attackRange : AttackRange.defaultFor(this);
    }
}
