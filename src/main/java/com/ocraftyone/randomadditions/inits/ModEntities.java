/*
 * Copyright (c) 2022 Ocraftyone
 *
 * View license here: https://gist.github.com/Ocraftyone/06f367618c202a79bc6309ee59250260
 */

package com.ocraftyone.randomadditions.inits;

import com.ocraftyone.randomadditions.Constants;
import com.ocraftyone.randomadditions.entities.ModifiedFishingHook;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MOD_ID);
    
    public static final RegistryObject<EntityType<ModifiedFishingHook>> FISHING_HOOK = register("fishing_hook", () -> EntityType.Builder.<ModifiedFishingHook>of(ModifiedFishingHook::new, MobCategory.MISC)
            .noSave()
            .noSummon()
            .sized(0.25F, 0.25F)
            .setTrackingRange(4)
            .setUpdateInterval(5));
    
    
    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> supplier) {
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID, name);
        return ENTITY_REGISTRY.register(name, () -> supplier.get().build(location.toString()));
    }
}
