package net.superkat.pumpkinpal.fabric;

import net.fabricmc.api.ModInitializer;
import net.superkat.pumpkinpal.PumpkinPal;
import software.bernie.geckolib.GeckoLib;

public class PumpkinPalFabric implements ModInitializer {
//    public static final EntityType<PumpkinPalEntity> PUMPKIN_PAL = Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(PumpkinPal.MOD_ID, "pumpkinpal"),
//            FabricEntityTypeBuilder.create(MobCategory.CREATURE, PumpkinPalEntity::new).dimensions(EntityDimensions.scalable(0.75f, 0.75f)).build());

//    public static Item PUMPKINPAL_SPAWN_EGG = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(PumpkinPal.MOD_ID, "pumpkinpal_spawn_egg"),
//            new SpawnEggItem(PUMPKIN_PAL, 0xffffff, 0xffffff, new Item.Properties()));

    @Override
    public void onInitialize() {
        PumpkinPal.init();

        GeckoLib.initialize();
    }
}
