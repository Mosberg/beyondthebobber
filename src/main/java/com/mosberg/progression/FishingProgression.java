package com.mosberg.progression;

import com.mosberg.BeyondtheBobber;
import com.mosberg.item.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class FishingProgression {
  private static final Map<String, Integer> playerFishingLevels = new HashMap<>();
  private static final Map<String, Integer> playerFishingXP = new HashMap<>();
  private static final Map<String, Long> playerFishingTime = new HashMap<>();
  private static final Map<String, Integer> playerFishCaught = new HashMap<>();
  private static final Map<String, Map<Item, Integer>> playerFishVarieties = new HashMap<>();

  // XP thresholds for each level (20 levels now)
  private static final int[] LEVEL_THRESHOLDS = {
      0, 100, 250, 500, 1000, 2000, 4000, 8000, 16000, 32000,
      64000, 100000, 150000, 200000, 300000, 450000, 600000, 800000, 1000000, 1500000
  };

  // Bonus multipliers based on fish rarity
  private static final Map<Item, Float> FISH_RARITY_MULTIPLIERS = Map.of(
      ModItems.TROPICAL_FISH, 1.0f,
      ModItems.DEEP_SEA_EEL, 1.5f,
      ModItems.FROST_SALMON, 1.5f,
      ModItems.SWAMP_PIRANHA, 2.0f,
      ModItems.LAVA_TROUT, 2.5f,
      ModItems.CRYSTAL_SHRIMP, 3.0f,
      ModItems.VOID_ANGLER, 4.0f,
      ModItems.GOLDEN_KOI, 5.0f,
      ModItems.SPECTRAL_JELLYFISH, 3.5f);

  public static void register() {
    BeyondtheBobber.LOGGER.info("Fishing Progression system initialized");
  }

  public static void onFishCaught(ServerPlayerEntity player, ItemStack fish) {
    String playerId = player.getUuidAsString();
    int currentXP = playerFishingXP.getOrDefault(playerId, 0);
    int currentLevel = getLevel(currentXP);
    Item item = fish.getItem();

    // Calculate XP with bonuses
    int baseXP = getBaseXP(item);
    float rarityMultiplier = FISH_RARITY_MULTIPLIERS.getOrDefault(item, 1.0f);
    float levelBonus = 1.0f + (currentLevel * 0.05f); // 5% bonus per level
    int xpGained = (int) (baseXP * rarityMultiplier * levelBonus);

    // Track statistics
    playerFishCaught.put(playerId, playerFishCaught.getOrDefault(playerId, 0) + 1);
    playerFishingTime.put(playerId, System.currentTimeMillis());

    // Track fish varieties
    Map<Item, Integer> varieties = playerFishVarieties.computeIfAbsent(playerId, k -> new HashMap<>());
    varieties.put(item, varieties.getOrDefault(item, 0) + 1);

    currentXP += xpGained;
    playerFishingXP.put(playerId, currentXP);

    int newLevel = getLevel(currentXP);

    // Send XP gain message
    player.sendMessage(Text.literal(
        "§6+§e" + xpGained + " §6Fishing XP §7(" + currentXP + "/" + getXPForNextLevel(currentLevel) + ")"), true);

    if (newLevel > currentLevel) {
      playerFishingLevels.put(playerId, newLevel);
      onLevelUp(player, newLevel);
    }
  }

  private static int getBaseXP(Item item) {
    if (item == ModItems.TROPICAL_FISH)
      return 5;
    if (item == ModItems.DEEP_SEA_EEL)
      return 10;
    if (item == ModItems.FROST_SALMON)
      return 10;
    if (item == ModItems.SWAMP_PIRANHA)
      return 15;
    if (item == ModItems.LAVA_TROUT)
      return 20;
    if (item == ModItems.CRYSTAL_SHRIMP)
      return 30;
    if (item == ModItems.VOID_ANGLER)
      return 50;
    if (item == ModItems.GOLDEN_KOI)
      return 75;
    if (item == ModItems.SPECTRAL_JELLYFISH)
      return 40;
    return 1;
  }

  private static void onLevelUp(ServerPlayerEntity player, int level) {
    // Visual and audio feedback
    player.getEntityWorld().playSound(
        null,
        player.getBlockPos(),
        SoundEvents.ENTITY_PLAYER_LEVELUP,
        SoundCategory.PLAYERS,
        1.0f,
        1.0f);

    player.sendMessage(Text.literal("§6§l⋆ §e§lFISHING LEVEL UP! §6§l⋆\n§7You are now level §e" + level + "§7!"),
        false);

    // Grant rewards
    grantLevelReward(player, level);

    // Grant perks
    applyLevelPerks(player, level);
  }

  private static void grantLevelReward(ServerPlayerEntity player, int level) {
    MinecraftServer server = player.getEntityWorld().getServer();
    if (server == null)
      return;

    switch (level) {
      case 2 -> {
        player.getInventory().offerOrDrop(new ItemStack(ModItems.FISHING_NET));
        player.sendMessage(Text.literal("§aUnlocked: §fFishing Net"), false);
      }
      case 4 -> {
        player.getInventory().offerOrDrop(new ItemStack(ModItems.FISHING_TRAP));
        player.sendMessage(Text.literal("§aUnlocked: §fFishing Trap"), false);
      }
      case 6 -> {
        player.getInventory().offerOrDrop(new ItemStack(ModItems.MASTER_FISHING_ROD));
        player.sendMessage(Text.literal("§aUnlocked: §fMaster Fishing Rod"), false);
      }
      case 8 -> {
        player.getInventory().offerOrDrop(new ItemStack(ModItems.RARE_TREASURE_MAP));
        player.sendMessage(Text.literal("§aUnlocked: §fRare Treasure Map"), false);
      }
      case 10 -> {
        ItemStack rod = new ItemStack(Items.FISHING_ROD);
        // Simple enchanted rod without complex registry access
        player.getInventory().offerOrDrop(rod);
        player.sendMessage(Text.literal("§aReward: §6Legendary Fishing Rod"), false);
      }
      case 15 -> {
        player.getInventory().offerOrDrop(new ItemStack(ModItems.FISH_FINDER));
        player.sendMessage(Text.literal("§aUnlocked: §fFish Finder"), false);
      }
      case 20 -> {
        player.getInventory().offerOrDrop(new ItemStack(ModItems.GOLDEN_KOI, 3));
        player.sendMessage(Text.literal("§6§l⋆ MASTER FISHERMAN ⋆\n§eYou've reached max level!"), false);
      }
    }
  }

  private static void applyLevelPerks(ServerPlayerEntity player, int level) {
    // Grant temporary buffs on level up
    if (level >= 5) {
      player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 12000, 0));
    }
    if (level >= 10) {
      player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 12000, level / 10));
    }
  }

  private static int getLevel(int xp) {
    for (int i = LEVEL_THRESHOLDS.length - 1; i >= 0; i--) {
      if (xp >= LEVEL_THRESHOLDS[i]) {
        return i + 1;
      }
    }
    return 1;
  }

  private static int getXPForNextLevel(int currentLevel) {
    if (currentLevel >= LEVEL_THRESHOLDS.length) {
      return LEVEL_THRESHOLDS[LEVEL_THRESHOLDS.length - 1];
    }
    return LEVEL_THRESHOLDS[currentLevel];
  }

  public static void reset() {
    playerFishingLevels.clear();
    playerFishingXP.clear();
    playerFishingTime.clear();
    playerFishCaught.clear();
    playerFishVarieties.clear();
  }

  public static int getLevel(ServerPlayerEntity player) {
    return playerFishingLevels.getOrDefault(player.getUuidAsString(), 1);
  }

  public static int getXP(ServerPlayerEntity player) {
    return playerFishingXP.getOrDefault(player.getUuidAsString(), 0);
  }

  public static int getTotalFishCaught(ServerPlayerEntity player) {
    return playerFishCaught.getOrDefault(player.getUuidAsString(), 0);
  }

  public static int getUniqueFishCaught(ServerPlayerEntity player) {
    Map<Item, Integer> varieties = playerFishVarieties.get(player.getUuidAsString());
    return varieties != null ? varieties.size() : 0;
  }
}
