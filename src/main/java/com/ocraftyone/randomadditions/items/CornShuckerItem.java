/*
 * Copyright (c) 2022 Ocraftyone
 *
 * View license here: https://gist.github.com/Ocraftyone/06f367618c202a79bc6309ee59250260
 */

package com.ocraftyone.randomadditions.items;

import com.ocraftyone.randomadditions.client.renderer.ShuckerItemModelRenderer;
import com.ocraftyone.randomadditions.inits.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.Random;
import java.util.function.Consumer;

public class CornShuckerItem extends Item implements CustomUseEffectsItem {
    public CornShuckerItem(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            ShuckerItemModelRenderer renderer = new ShuckerItemModelRenderer();
            
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return this.renderer;
            }
        });
    }
    
    
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.EAT;
    }
    
    @Override
    public int getUseDuration(ItemStack pStack) {
        return 32;
//        return 256;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        
        if (stack.getOrCreateTag()
                .contains("Shucking")) {
            pPlayer.startUsingItem(pUsedHand);
            return InteractionResultHolder.pass(stack);
        }
        
        InteractionHand otherHand = pUsedHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack itemInOtherHand = pPlayer.getItemInHand(otherHand);
        if (itemInOtherHand.is(ModItems.CORN_COB.get())) {
            ItemStack item = itemInOtherHand.copy();
            ItemStack toShuck = item.split(1);
            pPlayer.startUsingItem(pUsedHand);
            stack.getOrCreateTag()
                    .put("Shucking", toShuck.serializeNBT());
            pPlayer.setItemInHand(otherHand, item);
            return InteractionResultHolder.success(stack);
        }
        
        return InteractionResultHolder.fail(stack);
    }
    
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (!(pLivingEntity instanceof Player player)) return;
        CompoundTag tag = pStack.getOrCreateTag();
        if (tag.contains("Shucking")) {
            player.getInventory().placeItemBackInInventory(ItemStack.of(tag.getCompound("Shucking")));
            tag.remove("Shucking");
        }
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!(pLivingEntity instanceof Player player)) return pStack;
        if (pStack.getOrCreateTag().contains("Shucking")) {
            pStack.getOrCreateTag().remove("Shucking");
            pStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(pLivingEntity.getUsedItemHand()));
            player.getInventory().add(new ItemStack(ModItems.SHUCKED_CORN_COB.get()));
        }
        return pStack;
    }
    
    @Override
    public Boolean shouldTriggerUseEffects(ItemStack stack, LivingEntity entity) {
        return stack.getOrCreateTag().contains("Shucking");
    }
    
    @Override
    public boolean triggerUseEffects(ItemStack stack, LivingEntity entity, int count, Random random) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("Shucking")) {
            ItemStack toShuck = ItemStack.of(tag.getCompound("Shucking"));
            entity.spawnItemParticles(toShuck, count);
        }
        
        if ((entity.getTicksUsingItem() - 6) % 7 == 0)
            entity.playSound(SoundEvents.GRASS_STEP, 0.9F + 0.2F * random.nextFloat(),
                    random.nextFloat() * 0.2F + 0.9F);
        
        return true;
    }
}
