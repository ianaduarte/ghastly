package dev.ianaduarte.ghastly.client.renderer.layer;

import dev.ianaduarte.ceramic.geom.CeramicModel;
import dev.ianaduarte.ceramic.geom.PlanarOrientation;
import dev.ianaduarte.ceramic.geom.Transform;
import dev.ianaduarte.ceramic.geom.definitions.CeramicMeshDefinition;
import dev.ianaduarte.ceramic.geom.definitions.CeramicMeshDeformation;
import dev.ianaduarte.ceramic.geom.definitions.CeramicModelDefinition;
import dev.ianaduarte.ceramic.layers.CeramicModelLayer;
import dev.ianaduarte.ceramic.texture.CubeMapping;
import dev.ianaduarte.ceramic.texture.UVPlane;
import dev.ianaduarte.ghastly.Ghastly;
import dev.ianaduarte.ghastly.client.renderer.GhastRenderer;
import dev.ianaduarte.ghastly.client.renderer.state.GhastRenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GhastTentacleLayer extends CeramicModelLayer<GhastRenderState, GhastRenderer> {
	private static final ResourceLocation TEXTURE = Ghastly.getLocation("textures/entity/ghast/tentacles.png");
	public GhastTentacleLayer(GhastRenderer renderer, CeramicModel model) {
		super(renderer, RenderType::entityCutoutNoCull, model);
	}
	public static CeramicModelDefinition createModel() {
		CeramicModelDefinition modelDefinition = new CeramicModelDefinition(64, 32);
		
		UVPlane topPlane = UVPlane.of(52.5f, 0.5f, 0, 1, 1);
		UVPlane sidePlane = UVPlane.of(58, 8, 0, 12, 16);
		modelDefinition.addPart(
			"tentacle_core",
			new CeramicMeshDefinition()
				.addCuboid(12, 16, 12, -6, -15.25f, 6, CeramicMeshDeformation.NONE, CubeMapping.of(sidePlane, sidePlane, sidePlane, sidePlane, topPlane, null), false, false)
			, Transform.translation(0, -0.25f, 0)
		);
		modelDefinition.addPart(
			"tentacles",
			new CeramicMeshDefinition()
				//front
				.addPlane(PlanarOrientation.XY, 4, 16, -2, -14.75f, -7.0f, UVPlane.of( 3,  8, 0, 4, 16), false, false)
				.addPlane(PlanarOrientation.XY, 4, 16,  1, -14.75f, -7.5f, UVPlane.of( 3, 24, 0, 4, 16), false, false)
				.addPlane(PlanarOrientation.XY, 4, 16, -5, -14.75f, -7.5f, UVPlane.of(27,  8, 0, 4, 16), false, false)
				.addPlane(PlanarOrientation.XY, 4, 16,  3, -14.75f, -6.5f, UVPlane.of(27, 24, 0, 4, 16), false, false)
				.addPlane(PlanarOrientation.XY, 4, 16, -7, -14.75f, -6.5f, UVPlane.of( 3, 24, 0, 4, 16), false, false)
				//back
				.addPlane(PlanarOrientation.XY, 4, 16, -2, -14.75f, 7.0f, UVPlane.of( 3,  8, 0, 4, 16), true, false)
				.addPlane(PlanarOrientation.XY, 4, 16,  1, -14.75f, 7.5f, UVPlane.of( 3, 24, 0, 4, 16), true, false)
				.addPlane(PlanarOrientation.XY, 4, 16, -5, -14.75f, 7.5f, UVPlane.of(27,  8, 0, 4, 16), true, false)
				.addPlane(PlanarOrientation.XY, 4, 16,  3, -14.75f, 6.5f, UVPlane.of(27, 24, 0, 4, 16), true, false)
				.addPlane(PlanarOrientation.XY, 4, 16, -7, -14.75f, 6.5f, UVPlane.of( 3, 24, 0, 4, 16), true, false)
				//left
				.addPlane(PlanarOrientation.ZY, 4, 16, -7.0f, -14.75f, -2.0f, UVPlane.of( 3,  8, 0, 4, 16), false, false)
				.addPlane(PlanarOrientation.ZY, 4, 16, -7.5f, -14.75f,  2.0f, UVPlane.of( 3, 24, 0, 4, 16), false, false)
				.addPlane(PlanarOrientation.ZY, 4, 16, -7.0f, -14.75f,  6.0f, UVPlane.of(27, 24, 0, 4, 16), false, false)
				//right
				.addPlane(PlanarOrientation.ZY, 4, 16,  7.0f, -14.75f, -2.0f, UVPlane.of( 3,  8, 0, 4, 16), true, false)
				.addPlane(PlanarOrientation.ZY, 4, 16,  7.5f, -14.75f,  2.0f, UVPlane.of(27,  8, 0, 4, 16), true, false)
				.addPlane(PlanarOrientation.ZY, 4, 16,  7.0f, -14.75f,  6.0f, UVPlane.of( 3, 24, 0, 4, 16), true, false)
			, Transform.translation(0, -0.25f, 0)
		);
		return modelDefinition;
	}
	
	private static final int PERIODICITY = 15;
	@Override
	protected void poseModel(GhastRenderState ghast, float tickDelta, float currentTick) {
		int roundRobin = (int)((((ghast.ageInTicks + ghast.id) % PERIODICITY) / PERIODICITY) * 4);
		float xTilt = ghast.walkAnimationSpeed * -15;
		float zTilt = Mth.clamp(ghast.deltaMovement.x *  30, -30, 30);
		this.model.getPart("tentacles").uvOffset.u = roundRobin * 6;
		
		this.model.transform.xRot = xTilt * Mth.DEG_TO_RAD;
		this.model.transform.yPos = Math.max(Mth.abs(xTilt / 15), Mth.abs(zTilt / 30));
		this.model.transform.zRot = zTilt * Mth.DEG_TO_RAD;
	}
	@Override
	protected ResourceLocation getTextureLocation(GhastRenderState entity) {
		return TEXTURE;
	}
}
