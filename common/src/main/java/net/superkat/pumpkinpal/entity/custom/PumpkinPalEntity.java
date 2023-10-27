package net.superkat.pumpkinpal.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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

public class PumpkinPalEntity extends AbstractGolem implements GeoEntity {

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.pumpkinpal.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.pumpkinpal.walk");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("animation.pumpkinpal.run");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int ticksUntilNextPotion = -1;
    private boolean wardenNearby = false;

    public PumpkinPalEntity(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::idleAnimController));
    }

    protected <E extends PumpkinPalEntity>PlayState idleAnimController(final AnimationState<E> event) {
        if(event.isMoving()) {
            if(wardenNearby) {
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

    @Override
    public void aiStep() {
        super.aiStep();
        if(!this.level().isClientSide) {
            System.out.println(this.wardenNearby);
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
                    wardenNearby = false;
                    return;
                } else if (closestWarden != null && closestPlayer != null) {
                    wardenNearby = true;
                    target = this.random.nextBoolean() ? closestWarden : closestPlayer;
                } else if (closestPlayer == null) {
                    //Requires a warden to be nearby to throw potions
                    wardenNearby = true;
                    target = closestWarden;
                }
                if(target == null) {
                    wardenNearby = false;
                    return;
                }
                wardenNearby = true;
                Vec3 targetDeltaMovement = target.getDeltaMovement();
                double shootX = target.getX() + targetDeltaMovement.x - this.getX();
                double shootY = target.getEyeY() - (double) 1.1f - this.getY();
                double shootZ = target.getZ() + targetDeltaMovement.z - this.getZ();
                double h = Math.sqrt(shootX * shootX + shootZ * shootZ);

                Potion potion = decidePotion();
                ThrownPotion thrownPotion = new ThrownPotion(this.level(), this);
                thrownPotion.setItem(PotionUtils.setPotion(new ItemStack(Items.PUMPKIN_PIE), potion));
                thrownPotion.setXRot(thrownPotion.getXRot() + 20.0f);
                thrownPotion.shoot(shootX, shootY + h * 0.2 + 3, shootZ, 0.75f, 8.0f);
                this.level().addFreshEntity(thrownPotion);
            }
        }
    }

    public Potion decidePotion() {
        //Probably could be better but I'm on a time limit as of now
        ArrayList<Potion> possiblePotions = new ArrayList<>();
        possiblePotions.add(Potions.HEALING);
        possiblePotions.add(Potions.STRONG_HEALING);
        possiblePotions.add(Potions.HARMING);
        possiblePotions.add(Potions.STRENGTH);
        possiblePotions.add(Potions.WEAKNESS);
        possiblePotions.add(Potions.LEAPING);
        possiblePotions.add(Potions.STRONG_LEAPING);
        possiblePotions.add(Potions.SWIFTNESS);
        possiblePotions.add(Potions.STRONG_SWIFTNESS);
        possiblePotions.add(Potions.SLOWNESS);
        possiblePotions.add(Potions.SLOW_FALLING);
//        possiblePotions.add(Potions.INVISIBILITY);

        Potion returnPotion = possiblePotions.get(this.random.nextInt(possiblePotions.size()));

//        if(returnPotion == Potions.INVISIBILITY) {
//            if(this.random.nextBoolean()) {
//                MobEffectInstance glowing = new MobEffectInstance(MobEffects.GLOWING, 300, 0);
//                returnPotion = new Potion("glowing", glowing);
//            } else {
//                MobEffectInstance invis = new MobEffectInstance(MobEffects.GLOWING, 100, 0);
//                returnPotion = new Potion("invisibility", invis);
//            }
//        }
        return returnPotion;
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
        super.playSound(soundEvent, f, g);
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
                super.playSound(SoundEvents.AXOLOTL_DEATH, f * 1.2f, g);
                possiblyPlaySculkSound(f, g);
            }
        }
    }

    public void possiblyPlaySculkSound(float f, float g) {
        if(this.random.nextInt(1, 7) == 1) {
           super.playSound(SoundEvents.SCULK_CLICKING, f / 1.2f, g);
        }
    }



    //    @Override
//    public void push(Entity entity) {
//        if(entity instanceof Warden) {
//            return;
//        }
//        super.push(entity);
//    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
