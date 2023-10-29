package net.superkat.pumpkinpal.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.superkat.pumpkinpal.PumpkinPal;
import net.superkat.pumpkinpal.entity.custom.PumpkinPalEntity;
import software.bernie.geckolib.GeckoLib;

public class PumpkinPalFabric implements ModInitializer {

    public static final EntityType<PumpkinPalEntity> PUMPKIN_PAL = Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(PumpkinPal.MOD_ID, "pumpkinpal"),
            FabricEntityTypeBuilder.create(MobCategory.CREATURE, PumpkinPalEntity::new).dimensions(EntityDimensions.scalable(0.75f, 0.75f)).build());

    @Override
    public void onInitialize() {
        PumpkinPal.init();

        GeckoLib.initialize();

        FabricDefaultAttributeRegistry.register(PUMPKIN_PAL, PumpkinPalEntity.pumpkinPalAttributes());
    }
}
