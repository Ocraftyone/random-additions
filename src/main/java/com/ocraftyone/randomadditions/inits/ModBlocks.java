/*
 * Copyright (c) 2022 Ocraftyone
 *
 * View license here: https://gist.github.com/Ocraftyone/06f367618c202a79bc6309ee59250260
 */

package com.ocraftyone.randomadditions.inits;

import com.ocraftyone.randomadditions.Constants;
import com.ocraftyone.randomadditions.blocks.CornCrop;
import com.ocraftyone.randomadditions.blocks.CornShuckerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    
    public static final RegistryObject<Block> CORN_CROP = register("corn_crop", CornCrop::new, BlockBehaviour.Properties.of(Material.PLANT).noCollission().noOcclusion().randomTicks().instabreak().sound(SoundType.CROP), null, null, true);
    
    public static final RegistryObject<Block> AUTO_CORN_SHUCKER = register("auto_corn_shucker", CornShuckerBlock::new, BlockBehaviour.Properties.of(Material.HEAVY_METAL).sound(SoundType.METAL), Constants.GeneralTab, null, false);
    
    
    //Registration Methods
    private static <T extends Block> RegistryObject<T> register(String name, BlockTypeSupplier<T> blockTypeSupplier, BlockBehaviour.Properties blockProperties, @Nullable CreativeModeTab tab, @Nullable Item.Properties itemProperties, boolean bypassItemCreation) {
        RegistryObject<T> registryObject = BLOCK_REGISTRY.register(name, () -> blockTypeSupplier.create(blockProperties));
        if (!bypassItemCreation) {
            registerBlockItem(name, registryObject, tab, itemProperties);
        }
        return registryObject;
    }
    
    private static RegistryObject<Item> registerBlockItem(String name, RegistryObject<? extends Block> block, CreativeModeTab tab, Item.Properties itemProperties) {
        if (itemProperties == null) {
            itemProperties = new Item.Properties();
        }
        if (tab != null) {
            itemProperties.tab(tab);
        }
        Item.Properties finalItemProperties = itemProperties;
        return ModItems.ITEM_REGISTRY.register(name, () -> new BlockItem(block.get(), finalItemProperties));
    }
    
    @FunctionalInterface
    public interface BlockTypeSupplier<T extends Block> {
        T create(BlockBehaviour.Properties blockProperties);
    }
}