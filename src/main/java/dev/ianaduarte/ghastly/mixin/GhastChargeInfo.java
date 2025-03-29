package dev.ianaduarte.ghastly.mixin;

import dev.ianaduarte.ghastly.network.GhastDataPayload;
import dev.ianaduarte.ghastly.util.GhastChargeGetter;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ghast.class)
public abstract class GhastChargeInfo extends FlyingMob implements GhastChargeGetter, Enemy {
	@Unique private int chargeTimePrev;
	@Unique private int chargeTime;
	protected GhastChargeInfo(EntityType<? extends FlyingMob> entityType, Level level) {
		super(entityType, level);
	}
	
	@Override
	public void setCharge(int charge) {
		this.chargeTimePrev = this.chargeTime;
		this.chargeTime = charge;
	}
	
	@Override
	public float getCharged(float tickDelta) {
		return Mth.lerp(tickDelta, this.chargeTimePrev, this.chargeTime);
	}
	
	@Mixin(Ghast.GhastShootFireballGoal.class)
	static class FireballGoalMixin {
		@Shadow @Final private Ghast ghast;
		@Shadow public int chargeTime;
		
		@ModifyConstant(method = "tick", constant = @Constant(intValue = 20))
		private int increaseChargeTime(int constant) {
			return 40;
		}
		
		@Inject(method = "stop", at = @At("TAIL"))
		private void resetData(CallbackInfo ci) {
			GhastDataPayload payload = new GhastDataPayload(this.ghast.getId(), -1, 0);
			for(var player : PlayerLookup.tracking(this.ghast)) ServerPlayNetworking.send(player, payload);
		}
		
		@Inject(method = "tick", at = @At("TAIL"))
		private void fetchChargeTime(CallbackInfo ci) {
			LivingEntity target = this.ghast.getTarget();
			GhastDataPayload payload = new GhastDataPayload(this.ghast.getId(), target == null? -1 : target.getId(), this.chargeTime);
			
			for(var player : PlayerLookup.tracking(this.ghast)) ServerPlayNetworking.send(player, payload);
		}
	}
}
