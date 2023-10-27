package net.superkat.pumpkinpal.forge;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraftforge.fml.loading.FMLPaths;
import net.superkat.pumpkinpal.ExampleExpectPlatform;
import net.superkat.pumpkinpal.entity.custom.PumpkinPalEntity;

import java.nio.file.Path;

public class ExampleExpectPlatformImpl {
    /**
     * This is our actual method to {@link ExampleExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static void spawnPumpkinPal(Object level, BlockPos blockPos, BlockPattern.BlockPatternMatch blockPatternMatch) {
        PumpkinPalEntity pumpkinPalEntity = ExampleModForge.PUMPKINPAL.get().create((Level) level);
        if(pumpkinPalEntity != null) {
            BlockPos blockPos1 = blockPatternMatch.getBlock(0,2, 0).getPos();
            pumpkinPalEntity.moveTo(blockPos1.getX() + 0.5, blockPos1.getY() + 0.05, blockPos1.getZ() + 0.5, 0.0f, 0.0f);
            ((Level) level).addFreshEntity(pumpkinPalEntity);
            for (ServerPlayer serverPlayer : ((Level) level).getEntitiesOfClass(ServerPlayer.class, pumpkinPalEntity.getBoundingBox().inflate(5.0))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, pumpkinPalEntity);
            }
            CarvedPumpkinBlock.updatePatternBlocks((Level) level, blockPatternMatch);
        }
    }
}
