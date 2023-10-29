package net.superkat.pumpkinpal.forge;

import net.minecraftforge.fml.loading.FMLPaths;
import net.superkat.pumpkinpal.PumpkinPalPlatform;

import java.nio.file.Path;

public class PumpkinPalPlatformImpl {
    /**
     * This is our actual method to {@link PumpkinPalPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

//    public static void spawnPumpkinPal(Object level, BlockPos blockPos, BlockPattern.BlockPatternMatch blockPatternMatch) {
//        PumpkinPalEntity pumpkinPalEntity = PumpkinPalForge.PUMPKINPAL.get().create((Level) level);
//        if(pumpkinPalEntity != null) {
//            BlockPos blockPos1 = blockPatternMatch.getBlock(0,2, 0).getPos();
//            pumpkinPalEntity.moveTo(blockPos1.getX() + 0.5, blockPos1.getY() + 0.05, blockPos1.getZ() + 0.5, 0.0f, 0.0f);
//            ((Level) level).addFreshEntity(pumpkinPalEntity);
//            for (ServerPlayer serverPlayer : ((Level) level).getEntitiesOfClass(ServerPlayer.class, pumpkinPalEntity.getBoundingBox().inflate(5.0))) {
//                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, pumpkinPalEntity);
//            }
//            CarvedPumpkinBlock.updatePatternBlocks((Level) level, blockPatternMatch);
//        }
//    }
}
