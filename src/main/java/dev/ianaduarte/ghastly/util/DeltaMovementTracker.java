package dev.ianaduarte.ghastly.util;

import net.minecraft.world.phys.Vec3;

public interface DeltaMovementTracker {
	Vec3 getDeltaMovement(float tickDelta);
}
