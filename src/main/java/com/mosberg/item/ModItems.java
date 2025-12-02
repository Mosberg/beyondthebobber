package com.mosberg.item;

import com.mosberg.BeyondtheBobber;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
  // ==================== FISH ITEMS ====================
  public static final Item TROPICAL_FISH = registerItem("tropical_fish");
  public static final Item DEEP_SEA_EEL = registerItem("deep_sea_eel");
  public static final Item FROST_SALMON = registerItem("frost_salmon");
  public static final Item SWAMP_PIRANHA = registerItem("swamp_piranha");
  public static final Item LAVA_TROUT = registerItem("lava_trout");

  // New fish varieties
  public static final Item CRYSTAL_SHRIMP = registerItem("crystal_shrimp");
  public static final Item VOID_ANGLER = registerItem("void_angler");
  public static final Item GOLDEN_KOI = registerItem("golden_koi");
  public static final Item SPECTRAL_JELLYFISH = registerItem("spectral_jellyfish");

  // ==================== SPECIALIZED TOOLS ====================
  public static final Item FISHING_NET = registerItem("fishing_net",
      new Item.Settings().maxCount(1).maxDamage(64));
  public static final Item FISHING_TRAP = registerItem("fishing_trap",
      new Item.Settings().maxCount(1).maxDamage(128));
  public static final Item MASTER_FISHING_ROD = registerItem("master_fishing_rod",
      new Item.Settings().maxCount(1).maxDamage(512));
  public static final Item ENCHANTED_LURE = registerItem("enchanted_lure",
      new Item.Settings().maxCount(16));
  public static final Item FISH_FINDER = registerItem("fish_finder",
      new Item.Settings().maxCount(1));

  // ==================== TREASURE ITEMS ====================
  public static final Item ANCIENT_COIN = registerItem("ancient_coin");
  public static final Item SEA_GLASS = registerItem("sea_glass");
  public static final Item RARE_TREASURE_MAP = registerItem("rare_treasure_map");
  public static final Item PEARL = registerItem("pearl");
  public static final Item NAUTILUS_FRAGMENT = registerItem("nautilus_fragment");
  public static final Item OCEAN_SHARD = registerItem("ocean_shard");

  // ==================== CRAFTING MATERIALS ====================
  public static final Item FISH_SCALE = registerItem("fish_scale");
  public static final Item REINFORCED_LINE = registerItem("reinforced_line");
  public static final Item ENCHANTED_BAIT = registerItem("enchanted_bait");

  // ==================== CONSUMABLES ====================
  public static final Item FISHERMAN_POTION = registerItem("fisherman_potion",
      new Item.Settings().maxCount(16).food(ModFoodComponents.FISHERMAN_POTION));
  public static final Item COOKED_DEEP_SEA_EEL = registerItem("cooked_deep_sea_eel",
      new Item.Settings().food(ModFoodComponents.COOKED_DEEP_SEA_EEL));

  // ==================== ITEM GROUP ====================
  public static final RegistryKey<ItemGroup> BEYOND_THE_BOBBER_GROUP = RegistryKey.of(
      RegistryKeys.ITEM_GROUP,
      Identifier.of(BeyondtheBobber.MOD_ID, "beyond_the_bobber"));

  public static final ItemGroup BEYOND_THE_BOBBER_ITEM_GROUP = FabricItemGroup.builder()
      .icon(() -> new ItemStack(MASTER_FISHING_ROD))
      .displayName(Text.translatable("itemgroup.beyondthebobber.beyond_the_bobber"))
      .entries((context, entries) -> {
        // Fish
        entries.add(TROPICAL_FISH);
        entries.add(DEEP_SEA_EEL);
        entries.add(FROST_SALMON);
        entries.add(SWAMP_PIRANHA);
        entries.add(LAVA_TROUT);
        entries.add(CRYSTAL_SHRIMP);
        entries.add(VOID_ANGLER);
        entries.add(GOLDEN_KOI);
        entries.add(SPECTRAL_JELLYFISH);

        // Tools
        entries.add(FISHING_NET);
        entries.add(FISHING_TRAP);
        entries.add(MASTER_FISHING_ROD);
        entries.add(ENCHANTED_LURE);
        entries.add(FISH_FINDER);

        // Treasures
        entries.add(ANCIENT_COIN);
        entries.add(SEA_GLASS);
        entries.add(RARE_TREASURE_MAP);
        entries.add(PEARL);
        entries.add(NAUTILUS_FRAGMENT);
        entries.add(OCEAN_SHARD);

        // Materials
        entries.add(FISH_SCALE);
        entries.add(REINFORCED_LINE);
        entries.add(ENCHANTED_BAIT);

        // Consumables
        entries.add(FISHERMAN_POTION);
        entries.add(COOKED_DEEP_SEA_EEL);
      })
      .build();

  private static Item registerItem(String path) {
    return registerItem(path, new Item.Settings());
  }

  private static Item registerItem(String path, Item.Settings settings) {
    Identifier id = Identifier.of(BeyondtheBobber.MOD_ID, path);
    RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
    Item item = new Item(settings.registryKey(key));
    return Registry.register(Registries.ITEM, key, item);
  }

  public static void register() {
    BeyondtheBobber.LOGGER.info("Registering items for " + BeyondtheBobber.MOD_ID);
    Registry.register(Registries.ITEM_GROUP, BEYOND_THE_BOBBER_GROUP, BEYOND_THE_BOBBER_ITEM_GROUP);
  }
}
