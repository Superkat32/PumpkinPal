package net.superkat.pumpkinpal.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

public class PumpkinPalEntity extends PathfinderMob implements GeoEntity {

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.pumpkinpal.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.pumpkinpal.walk");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public PumpkinPalEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 5, this::idleAnimController));
    }

    protected <E extends PumpkinPalEntity>PlayState idleAnimController(final AnimationState<E> event) {
        if(event.isMoving()) {
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
    protected void doPush(Entity entity) {
        if(entity instanceof Warden) {
            return;
        }
        super.doPush(entity);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.AXOLOTL_IDLE_WATER;
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
