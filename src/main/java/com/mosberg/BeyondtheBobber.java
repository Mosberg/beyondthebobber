package com.mosberg;

import com.mosberg.item.ModItems;
import com.mosberg.loot.ModLootTables;
import com.mosberg.progression.FishingProgression;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeyondtheBobber implements ModInitializer {
	public static final String MOD_ID = "beyondthebobber";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		long startTime = System.currentTimeMillis();

		LOGGER.info("========================================");
		LOGGER.info("  Beyond the Bobber - Initializing");
		LOGGER.info("  Reeling in more than just fish...");
		LOGGER.info("========================================");

		// Register all custom items and item groups
		ModItems.register();

		// Register loot table modifications (biome-based fishing, treasure)
		ModLootTables.register();

		// Initialize fishing progression system
		FishingProgression.register();

		// Reset progression on world reload
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			LOGGER.info("Server stopped - resetting fishing progression data");
			FishingProgression.reset();
		});

		long endTime = System.currentTimeMillis();
		LOGGER.info("Beyond the Bobber initialized successfully in " + (endTime - startTime) + "ms");
	}
}
