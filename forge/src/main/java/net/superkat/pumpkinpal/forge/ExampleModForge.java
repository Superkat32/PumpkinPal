package net.superkat.pumpkinpal.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import net.superkat.pumpkinpal.ExampleMod;
import net.superkat.pumpkinpal.entity.client.PumpkinPalRenderer;
import net.superkat.pumpkinpal.entity.custom.PumpkinPalEntity;

@Mod(ExampleMod.MOD_ID)
public class ExampleModForge {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MOD_ID);

    public static final RegistryObject<EntityType<PumpkinPalEntity>> PUMPKINPAL = ENTITY_TYPES.register("pumpkinpal",
            () -> EntityType.Builder.of(PumpkinPalEntity::new, MobCategory.CREATURE).sized(0.75f, 0.75f).build("pumpkinpal"));

    public ExampleModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ExampleMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITY_TYPES.register(bus);

        ExampleMod.init();
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegisterAttributes {
        @SubscribeEvent
        public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
            AttributeSupplier.Builder pumpkinpalAttributes = PathfinderMob.createMobAttributes()
                    .add(Attributes.FOLLOW_RANGE, 16).add(Attributes.MAX_HEALTH, 15);

            event.put(PUMPKINPAL.get(), pumpkinpalAttributes.build());
        }
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegisterRenderer {
        @SubscribeEvent
        public static void registerRenderer(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(PUMPKINPAL.get(), PumpkinPalRenderer::new);
        }
    }
}
