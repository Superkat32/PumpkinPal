package net.superkat.pumpkinpal.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.superkat.pumpkinpal.PumpkinPal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    @Shadow public int experienceLevel;

    @Inject(method = "tick", at = @At("TAIL"))
    public void checkForXpGainEffect(CallbackInfo ci) {
        if(this.hasEffect(PumpkinPal.XP_GAIN.get())) {
                xpGainTick();
        }
    }

    public void xpGainTick() {
        if(!this.level().isClientSide) {
            int amplifier = Math.max((this.getEffect(PumpkinPal.XP_GAIN.get())).getAmplifier(), 1);
            if(this.experienceLevel < 30) {
                ExperienceOrb.award((ServerLevel)this.level(), this.position(), 1 + amplifier);
            } else {
                ExperienceOrb.award((ServerLevel)this.level(), this.position(), 1);
            }
        } else {
            for(int amount = 3; amount >= 0; amount--) {
                Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.TOTEM_OF_UNDYING, this.getX(), this.getEyeY(), this.getZ(), getRandomDouble(), getRandomDouble() / 2, getRandomDouble());
            }
            if(this.random.nextInt(1, 15) == 1) {
                Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.SCULK_SOUL, this.getX(), this.getY(), this.getZ(), 0.001, 0.2, 0.001);
            }
        }
    }

    public double getRandomDouble() {
        return this.random.nextDouble() * (this.random.nextBoolean() ? 1 : -1);
    }
}
