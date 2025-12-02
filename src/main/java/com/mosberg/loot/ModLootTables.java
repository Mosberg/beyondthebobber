package com.mosberg.loot;

import com.mosberg.BeyondtheBobber;
import com.mosberg.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModLootTables {
    private static final RegistryKey<LootTable> FISHING_GAMEPLAY_LOOT = RegistryKey.of(RegistryKeys.LOOT_TABLE,
            Identifier.of("minecraft", "gameplay/fishing"));
    private static final RegistryKey<LootTable> FISHING_TREASURE_LOOT = RegistryKey.of(RegistryKeys.LOOT_TABLE,
            Identifier.of("minecraft", "gameplay/fishing/treasure"));

    public static void register() {
        BeyondtheBobber.LOGGER.info("Registering loot table modifications for " + BeyondtheBobber.MOD_ID);

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin())
                return;

            // Standard fishing loot - all fish available
            if (key.equals(FISHING_GAMEPLAY_LOOT)) {
                // Common fish pool
                LootPool.Builder commonFishPool = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.TROPICAL_FISH).weight(10))
                        .with(ItemEntry.builder(ModItems.SWAMP_PIRANHA).weight(8))
                        .with(ItemEntry.builder(ModItems.DEEP_SEA_EEL).weight(6))
                        .with(ItemEntry.builder(ModItems.FROST_SALMON).weight(6))
                        .with(ItemEntry.builder(ModItems.CRYSTAL_SHRIMP).weight(4))
                        .rolls(ConstantLootNumberProvider.create(1));
                tableBuilder.pool(commonFishPool);

                // Rare fish pool
                LootPool.Builder rareFishPool = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.LAVA_TROUT).weight(5))
                        .with(ItemEntry.builder(ModItems.VOID_ANGLER).weight(3))
                        .with(ItemEntry.builder(ModItems.SPECTRAL_JELLYFISH).weight(4))
                        .conditionally(RandomChanceLootCondition.builder(0.15f))
                        .rolls(ConstantLootNumberProvider.create(1));
                tableBuilder.pool(rareFishPool);

                // Legendary fish pool (ultra rare)
                LootPool.Builder legendaryPool = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.GOLDEN_KOI).weight(1))
                        .conditionally(RandomChanceLootCondition.builder(0.01f))
                        .rolls(ConstantLootNumberProvider.create(1));
                tableBuilder.pool(legendaryPool);
            }

            // Enhanced treasure loot
            if (key.equals(FISHING_TREASURE_LOOT)) {
                LootPool.Builder treasurePool = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.ANCIENT_COIN).weight(10))
                        .with(ItemEntry.builder(ModItems.SEA_GLASS).weight(8))
                        .with(ItemEntry.builder(ModItems.RARE_TREASURE_MAP).weight(5))
                        .with(ItemEntry.builder(ModItems.PEARL).weight(6))
                        .with(ItemEntry.builder(ModItems.NAUTILUS_FRAGMENT).weight(4))
                        .with(ItemEntry.builder(ModItems.OCEAN_SHARD).weight(3))
                        .with(ItemEntry.builder(Items.DIAMOND).weight(2))
                        .with(ItemEntry.builder(Items.EMERALD).weight(3))
                        .with(ItemEntry.builder(ModItems.ENCHANTED_LURE).weight(5))
                        .with(ItemEntry.builder(ModItems.FISH_SCALE).weight(7))
                        .rolls(UniformLootNumberProvider.create(1, 2));
                tableBuilder.pool(treasurePool);
            }
        });
    }
}
