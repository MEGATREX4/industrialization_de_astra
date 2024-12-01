package com.megatrex4;

import earth.terrarium.adastra.api.systems.OxygenApi;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndustrializationDeAstra implements ModInitializer {
	public static final String MOD_ID = "industrialization_de_astra";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("Initialized Industrialization De Astra with custom oxygen and energy logic.");
	}
}
