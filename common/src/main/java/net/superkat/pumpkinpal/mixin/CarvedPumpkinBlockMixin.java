package net.superkat.pumpkinpal.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.superkat.pumpkinpal.ExampleExpectPlatform;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(CarvedPumpkinBlock.class)
public class CarvedPumpkinBlockMixin {

    @Shadow @Final private static Predicate<BlockState> PUMPKINS_PREDICATE;
    @Unique
    @Nullable
    private BlockPattern pumpkinPalPattern;

    @Unique
    private BlockPattern setOrCreatePumpkinPalPattern() {
        if(this.pumpkinPalPattern == null) {
            this.pumpkinPalPattern = BlockPatternBuilder.start()
                    .aisle("~", "^", "#")
                    .where('~', blockInWorld -> blockInWorld.getState().isAir())
                    .where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SCULK_CATALYST)))
                    .build();
        }

        return this.pumpkinPalPattern;
    }

    @Inject(method = "trySpawnGolem", at = @At("TAIL"))
    private void trySpawnPumpkinPal(Level level, BlockPos blockPos, CallbackInfo ci) {
        BlockPattern.BlockPatternMatch blockPatternMatch = this.setOrCreatePumpkinPalPattern().find(level, blockPos);
        if(blockPatternMatch != null) {
            ExampleExpectPlatform.spawnPumpkinPal(level, blockPos, blockPatternMatch);
            CarvedPumpkinBlock.clearPatternBlocks(level, blockPatternMatch);
        }
    }

}
