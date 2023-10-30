package net.superkat.pumpkinpal.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.superkat.pumpkinpal.PumpkinPal;
import net.superkat.pumpkinpal.entity.MoveTowardsPlayerGoal;
import net.superkat.pumpkinpal.entity.MoveTowardsWardenGoal;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Collections;

public class PumpkinPalEntity extends AbstractGolem implements GeoEntity {

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.pumpkinpal.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.pumpkinpal.walk");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("animation.pumpkinpal.run");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int ticksUntilNextPotion = -1;

    public PumpkinPalEntity(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::idleAnimController));
    }

    protected <E extends PumpkinPalEntity>PlayState idleAnimController(final AnimationState<E> event) {
        if(event.isMoving()) {
            @Nullable
            LivingEntity closestWarden = this.level().getNearestEntity(Warden.class, TargetingConditions.DEFAULT, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(32, 4.0, 32));
            if(closestWarden != null) {
                return event.setAndContinue(RUN_ANIM);
            }
            return event.setAndContinue(WALK_ANIM);
        } else if (!event.isMoving()) {
            return event.setAndContinue(IDLE_ANIM);
        } else {
            return PlayState.STOP;
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MoveTowardsWardenGoal(this, 1.5, 32));
//        this.goalSelector.addGoal(2, new PanicGoal(this, 2));
        this.goalSelector.addGoal(5, new MoveTowardsPlayerGoal(this, 0.7, 16));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(15, new RandomStrollGoal(this, 1));
//        super.registerGoals();
    }

    public static AttributeSupplier.Builder pumpkinPalAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.MAX_HEALTH, 15)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(!this.level().isClientSide) {
            //Sets the ticksUntilNextPotion as a Pumpkin Pal is spawned/loaded in
            if(ticksUntilNextPotion == -1) {
                ticksUntilNextPotion = this.random.nextInt(300, 1200);
            }
            if(ticksUntilNextPotion-- > 0) {
                return;
            }

            if(ticksUntilNextPotion-- <= 0) {
                ticksUntilNextPotion = this.random.nextInt(300, 1200);
                @Nullable
                LivingEntity target = null;
                @Nullable
                LivingEntity closestWarden = this.level().getNearestEntity(Warden.class, TargetingConditions.DEFAULT, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(32, 4.0, 32));
                @Nullable
                LivingEntity closestPlayer = this.level().getNearestPlayer(this, 16);

                if(closestWarden == null && closestPlayer == null) {
                    return;
                } else if (closestWarden != null && closestPlayer != null) {
                    target = this.random.nextBoolean() ? closestWarden : closestPlayer;
                } else if (closestPlayer == null) {
                    //Requires a warden to be nearby to throw potions
                    target = closestWarden;
                }
                if(target == null) {
                    return;
                }
                Vec3 targetDeltaMovement = target.getDeltaMovement();
                double shootX = target.getX() + targetDeltaMovement.x - this.getX();
                double shootY = target.getEyeY() - (double) 1.1f - this.getY();
                double shootZ = target.getZ() + targetDeltaMovement.z - this.getZ();
                double h = Math.sqrt(shootX * shootX + shootZ * shootZ);

                ThrownPie thrownPie = new ThrownPie(this.level(), this);
                thrownPie.setItem(PotionUtils.setCustomEffects(new ItemStack(PumpkinPal.SCULK_PUMPKIN_PIE.get()), Collections.singleton(possibleEffects())));
                thrownPie.setXRot(thrownPie.getXRot() + 20.0f);
                thrownPie.shoot(shootX, shootY + h * 0.2 + 3, shootZ, 0.75f, 8.0f);
                this.level().addFreshEntity(thrownPie);
            }
        }
    }

    public MobEffectInstance possibleEffects() {
        ArrayList<MobEffectInstance> effects = new ArrayList<>();

        //MobEffects.<effect>, <duration>, <ambient???(false by default)>, <showParticles>
        effects.add(new MobEffectInstance(MobEffects.HEAL, 1)); //Instant health
        effects.add(new MobEffectInstance(MobEffects.HEAL, 1, 2)); //Ultra instant health
        effects.add(new MobEffectInstance(MobEffects.HEALTH_BOOST, 280, 1)); //Regen
        effects.add(new MobEffectInstance(MobEffects.HARM, 1)); //Instant harm
        effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 280)); //Speed
        effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 160, 3)); //Ultra speed
        effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160)); //Slowness
        effects.add(new MobEffectInstance(MobEffects.JUMP, 160, 2)); //Jump boost
        effects.add(new MobEffectInstance(MobEffects.SLOW_FALLING, 280, 1)); //Slow falling
        effects.add(new MobEffectInstance(MobEffects.GLOWING, 600, 1)); //Glowing
        effects.add(new MobEffectInstance(MobEffects.INVISIBILITY, 300, 1, false, true)); //Invis (VERY SCARY ON WARDEN)
        effects.add(new MobEffectInstance(MobEffects.BLINDNESS, 120, 1)); //Blindness
        effects.add(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 1)); //Night vision
        effects.add(new MobEffectInstance(MobEffects.ABSORPTION, 480, 3)); //Golden hearts thing

        MobEffectInstance returnEffect = effects.get(this.random.nextInt(effects.size()));

        return returnEffect;
    }

    @Override
    protected void doPush(Entity entity) {
        if(entity instanceof Warden) {
            return;
        }
        super.doPush(entity);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.AXOLOTL_IDLE_AIR;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.AXOLOTL_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }

    @Override
    public void playSound(SoundEvent soundEvent, float f, float g) {
        super.playSound(soundEvent, f * 1.2f, g);
        if (!this.isSilent()) {
            if(soundEvent == getAmbientSound()) {
                super.playSound(SoundEvents.WARDEN_AMBIENT, f / 1.3f, g);
                possiblyPlaySculkSound(f, g);
            }
            if(soundEvent == SoundEvents.AXOLOTL_HURT) {
                super.playSound(SoundEvents.WARDEN_HURT, f / 1.3f, g);
                super.playSound(SoundEvents.WARDEN_HEARTBEAT, f, g);
                possiblyPlaySculkSound(f, g);
            }
            if(soundEvent == getDeathSound()) {
                super.playSound(SoundEvents.AXOLOTL_DEATH, f * 1.7f, g);
                possiblyPlaySculkSound(f, g);
            }
        }
    }

    public void possiblyPlaySculkSound(float f, float g) {
        if(this.random.nextInt(1, 7) == 1) {
           super.playSound(SoundEvents.SCULK_CLICKING, f / 1.2f, g);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
