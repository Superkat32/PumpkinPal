package net.superkat.pumpkinpal.entity;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MoveTowardsPlayerGoal extends MoveTowardsTargetGoal {
    private final PathfinderMob mob;
    @Nullable
    private Player closestPlayer;
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    private final double speedModifier;
    private final float within;
    public MoveTowardsPlayerGoal(PathfinderMob pathfinderMob, double speedModifier, float within) {
        super(pathfinderMob, speedModifier, within);
        this.mob = pathfinderMob;
        this.speedModifier = speedModifier;
        this.within = within;
    }

    @Override
    public boolean canUse() {
        this.closestPlayer = this.mob.level().getNearestPlayer(this.mob, 16);
        if (this.closestPlayer == null) {
            return false;
        } else if (this.closestPlayer.distanceToSqr(this.mob) > (double)(this.within * this.within)) {
            return false;
        } else {
            Vec3 vec3 = DefaultRandomPos.getPosTowards(this.mob, 4, 2, this.closestPlayer.position(), 0.1);
            if (vec3 == null) {
                return false;
            } else {
                this.wantedX = vec3.x;
                this.wantedY = vec3.y;
                this.wantedZ = vec3.z;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && this.closestPlayer.isAlive() && this.closestPlayer.distanceToSqr(this.mob) < (double)(this.within * this.within);
    }

    @Override
    public void stop() {
        this.closestPlayer = null;
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }
}
