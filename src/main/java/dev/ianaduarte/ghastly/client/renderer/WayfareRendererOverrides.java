package dev.ianaduarte.ghastly.client.renderer;

import dev.ianaduarte.ceramic.renderer.CeramicRendererOverrides;
import net.minecraft.world.entity.EntityType;

public class WayfareRendererOverrides {
	public static void registerOverrides() {
		CeramicRendererOverrides.register(EntityType.GHAST, GhastRenderer::new);
	}
}
