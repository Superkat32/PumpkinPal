package net.superkat.pumpkinpal.entity.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.superkat.pumpkinpal.PumpkinPal;

public class ThrownPie extends ThrownPotion {
    public ThrownPie(Level level, LivingEntity livingEntity) {
        super(level, livingEntity);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        drop();
        super.onHitBlock(blockHitResult);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        drop();
        super.onHit(hitResult);
    }

    public void drop() {
        ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), PumpkinPal.SCULK_PUMPKIN_PIE.get().getDefaultInstance());
        itemEntity.setDefaultPickUpDelay();
        this.level().addFreshEntity(itemEntity);
    }
}
