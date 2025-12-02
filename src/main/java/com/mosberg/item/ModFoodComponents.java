package com.mosberg.item;

import net.minecraft.component.type.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent COOKED_DEEP_SEA_EEL = new FoodComponent.Builder()
            .nutrition(8)
            .saturationModifier(0.8f)
            .build();

    public static final FoodComponent FISHERMAN_POTION = new FoodComponent.Builder()
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEdible()
            .build();
}
