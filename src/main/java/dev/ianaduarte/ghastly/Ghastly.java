package dev.ianaduarte.ghastly;

import dev.ianaduarte.ghastly.network.GhastDataPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ghastly implements ModInitializer {
	public static final String MOD_ID = "ghastly";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static Logger getLogger(String id) {
		return LoggerFactory.getLogger(MOD_ID + ':' + id);
	}
	public static ResourceLocation getLocation(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
	public static String getStringLocation(String path){
		return MOD_ID + ':' + path;
	}

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playS2C().register(GhastDataPayload.TYPE, GhastDataPayload.STREAM_CODEC);
	}
}