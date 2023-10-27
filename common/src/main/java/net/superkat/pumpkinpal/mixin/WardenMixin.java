package net.superkat.pumpkinpal.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.superkat.pumpkinpal.entity.custom.PumpkinPalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Warden.class)
public class WardenMixin {

    @Inject(method = "canTargetEntity", at = @At("TAIL"), cancellable = true)
    public void wardenKeepsPumpkinPalSafe(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof PumpkinPalEntity) {
            cir.setReturnValue(false);
        }
    }

}
