/*
 * Copyright (c) 2022 Ocraftyone
 *
 * View license here: https://gist.github.com/Ocraftyone/06f367618c202a79bc6309ee59250260
 */

package com.ocraftyone.randomadditions.mixin;

import com.ocraftyone.randomadditions.items.CustomUseEffectsItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class CustomItemUseEffectsMixin extends Entity {
    
    //FROM CREATE MOD, USED UNDER MIT LICENSE
    //https://github.com/Creators-of-Create/Create
    
    private CustomItemUseEffectsMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Shadow
    public abstract ItemStack getUseItem();
    
    @Inject(method = "shouldTriggerItemUseEffects()Z", at = @At("HEAD"), cancellable = true)
    private void onShouldTriggerUseEffects(CallbackInfoReturnable<Boolean> cir) {
        ItemStack using = getUseItem();
        Item item = using.getItem();
        if (item instanceof CustomUseEffectsItem handler) {
            Boolean result = handler.shouldTriggerUseEffects(using, (LivingEntity) (Object) this);
            if (result != null) {
                cir.setReturnValue(result);
            }
        }
    }
    
    @Inject(method = "triggerItemUseEffects(Lnet/minecraft/world/item/ItemStack;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/UseAnim;", ordinal = 0), cancellable = true)
    private void onTriggerUseEffects(ItemStack stack, int count, CallbackInfo ci) {
        Item item = stack.getItem();
        if (item instanceof CustomUseEffectsItem handler) {
            if (handler.triggerUseEffects(stack, (LivingEntity) (Object) this, count, random)) {
                ci.cancel();
            }
        }
    }
}
