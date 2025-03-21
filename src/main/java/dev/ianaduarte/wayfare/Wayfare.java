package dev.ianaduarte.wayfare;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wayfare implements ModInitializer {
	public static final String MOD_ID = "wayfare";
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
	}
}