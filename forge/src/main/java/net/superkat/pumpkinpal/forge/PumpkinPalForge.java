package net.superkat.pumpkinpal.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.superkat.pumpkinpal.PumpkinPal;
import net.superkat.pumpkinpal.entity.client.PumpkinPalRenderer;
import net.superkat.pumpkinpal.entity.custom.PumpkinPalEntity;

@Mod(PumpkinPal.MOD_ID)
public class PumpkinPalForge {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PumpkinPal.MOD_ID);

    public static final RegistryObject<EntityType<PumpkinPalEntity>> PUMPKINPAL = ENTITY_TYPES.register("pumpkinpal",
            () -> EntityType.Builder.of(PumpkinPalEntity::new, MobCategory.CREATURE).sized(0.75f, 0.75f).build("pumpkinpal"));

    public PumpkinPalForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(PumpkinPal.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITY_TYPES.register(bus);

        PumpkinPal.init();
    }

    @Mod.EventBusSubscriber(modid = PumpkinPal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegisterAttributes {
        @SubscribeEvent
        public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
            AttributeSupplier.Builder pumpkinpalAttributes = PumpkinPalEntity.pumpkinPalAttributes();

            event.put(PUMPKINPAL.get(), pumpkinpalAttributes.build());
        }
    }

    @Mod.EventBusSubscriber(modid = PumpkinPal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegisterRenderer {
        @SubscribeEvent
        public static void registerRenderer(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(PUMPKINPAL.get(), PumpkinPalRenderer::new);
        }
    }
}
