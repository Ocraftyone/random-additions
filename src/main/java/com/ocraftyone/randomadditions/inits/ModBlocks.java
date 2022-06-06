package com.ocraftyone.randomadditions.inits;

import com.ocraftyone.randomadditions.Constants;
import com.ocraftyone.randomadditions.blocks.CornCrop;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    
    public static final RegistryObject<Block> CORN_CROP = BLOCK_REGISTRY.register("corn_crop", () -> new CornCrop(BlockBehaviour.Properties.of(Material.PLANT), null));
    
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, CreativeModeTab tab, Item.Properties itemProperties) {
        RegistryObject<T> registryObject = BLOCK_REGISTRY.register(name, supplier);
        registerBlockItem(name, registryObject, tab, itemProperties);
        return registryObject;
    }
    
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, CreativeModeTab tab) {
        return register(name, supplier, tab, null);
    }
    
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties itemProperties) {
        return register(name, supplier, null, itemProperties);
    }
    
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return register(name, supplier, null, null);
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
}
