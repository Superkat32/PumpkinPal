package net.superkat.pumpkinpal.entity;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MoveTowardsWardenGoal extends MoveTowardsTargetGoal {

    private final PathfinderMob mob;
    @Nullable
    private Warden closestWarden;
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    private boolean farAway;
    private final double speedModifier;
    private final float within;
    public MoveTowardsWardenGoal(PathfinderMob pathfinderMob, double speed, float radius) {
        super(pathfinderMob, speed, radius);
        this.mob = pathfinderMob;
        this.speedModifier = speed;
        this.within = radius;
    }

    @Override
    public boolean canUse() {
        this.closestWarden = this.mob.level().getNearestEntity(Warden.class, TargetingConditions.DEFAULT, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(within, 4.0, within));
//        List<Warden> nearbyWardens = this.mob.level().getNearbyEntities(Warden.class, TargetingConditions.DEFAULT.range(32.0), this.mob, this.mob.getBoundingBox().inflate(32.0, 4, 32.0));
//        if(nearbyWardens.isEmpty()) {
//            return false;
//        }
//        this.closestWarden = nearbyWardens.get(this.mob.getRandom().nextInt(nearbyWardens.size()));
        if (this.closestWarden == null) {
            return false;
        } else if (this.closestWarden.distanceToSqr(this.mob) > (double)(this.within * this.within)) {
            return false;
        } else {
            Vec3 vec3 = DefaultRandomPos.getPosTowards(this.mob, 5, 2, this.closestWarden.position(), 0.05);
            if (vec3 == null) {
                return false;
            } else {
                this.wantedX = vec3.x;
                this.wantedY = vec3.y;
                this.wantedZ = vec3.z;

                this.farAway = this.mob.distanceTo(closestWarden) > 5;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && this.closestWarden.isAlive() && this.closestWarden.distanceToSqr(this.mob) < (double)(this.within * this.within);
    }

    @Override
    public void stop() {
        this.closestWarden = null;
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, farAway ? speedModifier : 0.6);
    }
}
