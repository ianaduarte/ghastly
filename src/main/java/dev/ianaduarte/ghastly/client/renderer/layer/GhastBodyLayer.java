package dev.ianaduarte.ghastly.client.renderer.layer;

import dev.ianaduarte.ceramic.geom.CeramicModel;
import dev.ianaduarte.ceramic.geom.Transform;
import dev.ianaduarte.ceramic.geom.definitions.CeramicMeshDefinition;
import dev.ianaduarte.ceramic.geom.definitions.CeramicMeshDeformation;
import dev.ianaduarte.ceramic.geom.definitions.CeramicModelDefinition;
import dev.ianaduarte.ceramic.layers.CeramicModelLayer;
import dev.ianaduarte.ceramic.texture.CubeMapping;
import dev.ianaduarte.ghastly.client.renderer.GhastRenderer;
import dev.ianaduarte.ghastly.client.renderer.state.GhastRenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class GhastBodyLayer extends CeramicModelLayer<GhastRenderState, GhastRenderer> {
	public GhastBodyLayer(GhastRenderer renderer, CeramicModel model) {
		super(renderer, RenderType::entityCutoutNoCull, model);
	}
	public static CeramicModelDefinition createModel() {
		CeramicModelDefinition modelDefinition = new CeramicModelDefinition(64, 64);
		modelDefinition.addPart(
			"head",
			new CeramicMeshDefinition()
				.addCuboid(16, 16, 16, -8, 0, 8, CeramicMeshDeformation.NONE, CubeMapping.vanilla(0, 0, 16, 16, 16), false, false)
				.addCuboid(16, 16, 16, -8, 0, 8, CeramicMeshDeformation.EXPAND_QUARTER, CubeMapping.vanilla(0, 32, 16, 16, 16), false, false)
			, Transform.translation(0, 0, 0)
		);
		return modelDefinition;
	}
	@Override
	protected void poseModel(GhastRenderState ghast, float tickDelta, float currentTick) {
		float tilt = ghast.targetDelta.lengthSquared() == 0? 0 : ghast.targetDelta.y / ghast.targetDelta.length();
		//float tilt = ghast.chargeProgress * 15 * Mth.sin(currentTick * (0.4f * 0.4f * ghast.chargeProgress));
		
		this.model.transform.xRot = tilt * 20 * Mth.DEG_TO_RAD;
	}
}
