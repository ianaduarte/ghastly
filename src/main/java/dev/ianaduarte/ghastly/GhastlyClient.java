package dev.ianaduarte.ghastly;

import dev.ianaduarte.ghastly.client.renderer.layer.WayfareModelLayers;
import dev.ianaduarte.ghastly.client.renderer.WayfareRendererOverrides;
import dev.ianaduarte.ghastly.network.GhastDataPayload;
import dev.ianaduarte.ghastly.util.GhastChargeGetter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ghast;

public class GhastlyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		WayfareModelLayers.registerLayers();
		WayfareRendererOverrides.registerOverrides();
		
		ClientPlayNetworking.registerGlobalReceiver(GhastDataPayload.TYPE, (payload, context) -> {
			ClientLevel level = context.client().level;
			if(level == null) return;
			
			Entity entity = level.getEntity(payload.ghastId());
			if(entity instanceof Ghast ghast) {
				((GhastChargeGetter)ghast).setCharge(payload.chargeTime());
				
				if(payload.targetId() == -1) {
					ghast.setTarget(null);
				} else {
					Entity target = level.getEntity(payload.targetId());
					if(target instanceof LivingEntity livingTarget) ghast.setTarget(livingTarget);
				}
			}
		});
	}
}
