package dev.ianaduarte.ghastly.mixin;

import dev.ianaduarte.ghastly.util.DeltaMovementTracker;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDeltaTracker extends Entity implements Attackable, DeltaMovementTracker {
	//@Shadow private Vec3 deltaMovement;
	@Unique Vector3f deltaMovementPrev = new Vector3f();
	
	protected LivingEntityDeltaTracker(EntityType<? extends FlyingMob> entityType, Level level) {
		super(entityType, level);
	}
	
	@Override
	public Vec3 getDeltaMovement(float tickDelta) {
		Vec3 deltaMovement = this.getDeltaMovement();
		return new Vec3(
			Mth.lerp(tickDelta, this.deltaMovementPrev.x, deltaMovement.x),
			Mth.lerp(tickDelta, this.deltaMovementPrev.y, deltaMovement.y),
			Mth.lerp(tickDelta, this.deltaMovementPrev.z, deltaMovement.z)
		);
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void storePrevDelta(CallbackInfo ci) {
		Vec3 deltaMovement = this.getDeltaMovement();
		this.deltaMovementPrev.x = (float)Mth.lerp(0.5, this.deltaMovementPrev.x, deltaMovement.x);
		this.deltaMovementPrev.y = (float)Mth.lerp(0.5, this.deltaMovementPrev.y, deltaMovement.y);
		this.deltaMovementPrev.z = (float)Mth.lerp(0.5, this.deltaMovementPrev.z, deltaMovement.z);
	}
}
