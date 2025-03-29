package dev.ianaduarte.ghastly.client.renderer.layer;

import dev.ianaduarte.ceramic.layers.CeramicModelLayers;
import dev.ianaduarte.ghastly.Ghastly;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class WayfareModelLayers {
	public static final ModelLayerLocation GHAST_BODY_LAYER = register("ghast", "body", GhastBodyLayer::createModel);
	public static final ModelLayerLocation GHAST_TENTACLE_LAYER = register("ghast", "tentacles", GhastTentacleLayer::createModel);
	
	public static void registerLayers() {
	}
	
	private static ModelLayerLocation register(String path, CeramicModelLayers.ModelProvider provider) {
		return register(path, "main", provider);
	}
	private static ModelLayerLocation register(String path, String model, CeramicModelLayers.ModelProvider provider) {
		return CeramicModelLayers.registerModelDefinition(Ghastly.getLocation(path), model, provider);
	}
}
