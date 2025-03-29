package dev.ianaduarte.ghastly.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ianaduarte.ceramic.layers.CeramicModelLayers;
import dev.ianaduarte.ceramic.renderer.CeramicRenderer;
import dev.ianaduarte.ceramic.util.CMth;
import dev.ianaduarte.ghastly.Ghastly;
import dev.ianaduarte.ghastly.client.renderer.layer.GhastBodyLayer;
import dev.ianaduarte.ghastly.client.renderer.layer.GhastTentacleLayer;
import dev.ianaduarte.ghastly.client.renderer.layer.WayfareModelLayers;
import dev.ianaduarte.ghastly.client.renderer.state.GhastRenderState;
import dev.ianaduarte.ghastly.util.DeltaMovementTracker;
import dev.ianaduarte.ghastly.util.GhastChargeGetter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GhastRenderer extends CeramicRenderer<Ghast, GhastRenderState> {
	private static final ResourceLocation NORMAL_TEXTURE = Ghastly.getLocation("textures/entity/ghast/normal.png");
	private static final ResourceLocation AGGRO_TEXTURE = Ghastly.getLocation("textures/entity/ghast/aggro.png");
	private static final ResourceLocation[] CHARGE_TEXTURES = {
		Ghastly.getLocation("textures/entity/ghast/charge_1.png"),
		Ghastly.getLocation("textures/entity/ghast/charge_2.png"),
		Ghastly.getLocation("textures/entity/ghast/charge_3.png")
	};
	
	public GhastRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.renderLayers.add(new GhastBodyLayer(this, CeramicModelLayers.bakeModelDefinition(WayfareModelLayers.GHAST_BODY_LAYER)));
		this.renderLayers.add(new GhastTentacleLayer(this, CeramicModelLayers.bakeModelDefinition(WayfareModelLayers.GHAST_TENTACLE_LAYER)));
		this.shadowRadius = 1.5f;
	}
	
	@Override
	public GhastRenderState createRenderState() {
		return new GhastRenderState();
	}
	
	@Override
	public void extractRenderState(Ghast ghast, GhastRenderState ghastState, float partialTick) {
		super.extractRenderState(ghast, ghastState, partialTick);

		Vec3 deltaMovement = ((DeltaMovementTracker)ghast).getDeltaMovement(partialTick);
		Vec3 viewVector = ghast.getViewVector(partialTick);
		ghastState.id = ghast.getId();
		ghastState.viewVector.set(viewVector.x, viewVector.y, viewVector.z);
		ghastState.deltaMovement.set(deltaMovement.x, deltaMovement.y, deltaMovement.z).rotateY(Mth.lerp(partialTick, ghast.yBodyRotO, ghast.yBodyRot) * Mth.DEG_TO_RAD);
		ghastState.chargeProgress = Math.max(((GhastChargeGetter)ghast).getCharged(partialTick), 0) / 40f;
		
		if(ghast.getTarget() != null) {
			Vec3 tpos = ghast.getTarget().position();
			
			ghastState.aiState = ghast.isCharging()? GhastRenderState.AiState.CHARGING : GhastRenderState.AiState.AGGRO;
			ghastState.targetDelta.set(
				tpos.x - ghast.position().x,
				tpos.y - ghast.position().y,
				tpos.z - ghast.position().z
			);
		} else {
			ghastState.aiState = GhastRenderState.AiState.IDLE;
			ghastState.targetDelta.set(0);
			ghastState.chargeProgress = 0;
		}
	}
	
	private static final float SCALE = 4.5f;
	@Override
	protected void scale(GhastRenderState ghast, PoseStack poseStack) {
		float chargeScale = ghast.chargeProgress * 0.625f;
		chargeScale *= chargeScale;
		
		float gScale = 1 + ((ghast.id % 11) / 40f - 0.125f);
		poseStack.scale(SCALE * gScale - chargeScale, SCALE * gScale + chargeScale, SCALE * gScale - chargeScale);
	}
	
	@Override
	protected AABB getBoundingBoxForCulling(Ghast minecraft) {
		return super.getBoundingBoxForCulling(minecraft).expandTowards(0, -3, 0);
	}
	
	@Override
	protected void setupRotations(GhastRenderState ghast, PoseStack poseStack) {
		super.setupRotations(ghast, poseStack, ghast.bodyRot, ghast.scale, 160);
	}
	
	@Override
	public ResourceLocation getTextureLocation(GhastRenderState ghast) {
		return switch(ghast.aiState) {
			case IDLE -> NORMAL_TEXTURE;
			case AGGRO -> AGGRO_TEXTURE;
			case CHARGING -> CHARGE_TEXTURES[Math.round(ghast.chargeProgress * 2)];
		};
	}
	
	@Override
	public float getWhiteOverlayProgress(GhastRenderState ghast) {
		float nearEndCharge = CMth.remapRange(Math.max(ghast.chargeProgress, 0.75f), 0.75f, 1, 0, 1);
		return nearEndCharge * nearEndCharge;
	}
}
