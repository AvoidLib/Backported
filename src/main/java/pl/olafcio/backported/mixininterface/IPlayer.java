package pl.olafcio.backported.mixininterface;

import net.minecraft.world.phys.AABB;

public interface IPlayer {
    boolean backported_attr$isWithinAttackRange(AABB aABB, double d);
}
