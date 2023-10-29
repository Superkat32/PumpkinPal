package net.superkat.pumpkinpal.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.superkat.pumpkinpal.PumpkinPal;

@Mod(PumpkinPal.MOD_ID)
public class PumpkinPalForge {

//    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PumpkinPal.MOD_ID);

//    public static final RegistryObject<EntityType<PumpkinPalEntity>> PUMPKINPAL = ENTITY_TYPES.register("pumpkinpal",
//            () -> EntityType.Builder.of(PumpkinPalEntity::new, MobCategory.CREATURE).sized(0.75f, 0.75f).build("pumpkinpal"));

    public PumpkinPalForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(PumpkinPal.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

//        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
//        PumpkinPal.ENTITY_TYPES.register(bus);

        PumpkinPal.init();
    }

//    @Mod.EventBusSubscriber(modid = PumpkinPal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class RegisterAttributes {
//        @SubscribeEvent
//        public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
//
//            event.put(PumpkinPal.PUMPKINPAL.get(), pumpkinpalAttributes.build());
//        }
//    }

//    @Mod.EventBusSubscriber(modid = PumpkinPal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//    public static class RegisterRenderer {
//        @SubscribeEvent
//        public static void registerRenderer(final EntityRenderersEvent.RegisterRenderers event) {
//            event.registerEntityRenderer(PumpkinPal.PUMPKINPAL.get(), PumpkinPalRenderer::new);
//        }
//    }
}
