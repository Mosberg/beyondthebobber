package com.mosberg.mixin;

import com.mosberg.item.ModItems;
import com.mosberg.progression.FishingProgression;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {

  @Shadow
  public abstract PlayerEntity getPlayerOwner();

  /**
   * Inject into the use method which is called when the player reels in the
   * fishing rod.
   * This method returns the number of items caught.
   */
  @Inject(method = "use", at = @At("RETURN"))
  private void onUse(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
    PlayerEntity player = this.getPlayerOwner();

    // Check if player is on server side and using Master Fishing Rod
    if (player instanceof ServerPlayerEntity serverPlayer) {
      if (usedItem.getItem() == ModItems.MASTER_FISHING_ROD) {
        FishingProgression.getLevel(serverPlayer);

        // Grant bonus XP for using Master Fishing Rod
        FishingProgression.onFishCaught(serverPlayer, new ItemStack(ModItems.TROPICAL_FISH));

        // Increase treasure chance based on level (handled by loot tables)
        // This is just to track progression
      }
    }
  }
}
