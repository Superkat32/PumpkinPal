package net.superkat.pumpkinpal;

import com.google.common.base.Suppliers;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.hooks.item.food.FoodPropertiesHooks;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.superkat.pumpkinpal.entity.client.PumpkinPalRenderer;
import net.superkat.pumpkinpal.entity.custom.PumpkinPalEntity;

import java.util.function.Supplier;

public class PumpkinPal {
    public static final String MOD_ID = "pumpkinpal";

    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(MOD_ID, Registries.ENTITY_TYPE);
    public static final RegistrySupplier<EntityType<PumpkinPalEntity>> PUMPKINPAL = ENTITY_TYPES.register("pumpkinpal", Suppliers.memoize(() ->
            EntityType.Builder.of(PumpkinPalEntity::new, MobCategory.CREATURE).sized(0.75f, 0.75f).build("pumpkinpal")));

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(MOD_ID, Registries.MOB_EFFECT);
    public static final RegistrySupplier<MobEffect> XP_GAIN = MOB_EFFECTS.register("xp_gain", () ->
            new MobEffect(MobEffectCategory.BENEFICIAL, 0x8eff8a) {
    });

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
    public static final RegistrySupplier<Item> PUMPKINPAL_SPAWN_EGG = ITEMS.register("pumpkinpal_spawn_egg", () ->
            new ArchitecturySpawnEggItem(PUMPKINPAL, 0xffffff, 0xffffff, new Item.Properties().arch$tab(CreativeModeTabs.SPAWN_EGGS)));
    public static final RegistrySupplier<Item> SCULK_PUMPKIN_PIE = ITEMS.register("sculk_pumpkin_pie", () -> {
        FoodProperties.Builder fpBulider = new FoodProperties.Builder().nutrition(8).saturationMod(0.3f).alwaysEat();
        FoodPropertiesHooks.effect(fpBulider, () -> new MobEffectInstance(XP_GAIN.get(), 20, 2), 1);
        return new Item(new Item.Properties().rarity(Rarity.UNCOMMON).food(fpBulider.build()).arch$tab(CreativeModeTabs.FOOD_AND_DRINKS));
    });


    // Registering a new creative tab
//    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);
//    public static final RegistrySupplier<CreativeModeTab> PUMPKINPAL_TAB = TABS.register("pumpkinpal_tab", () ->
//            CreativeTabRegistry.create(Component.translatable("itemGroup." + MOD_ID + ".pumpkinpal_tab"),
//                    () -> new ItemStack(PumpkinPal.PUMPKINPAL_SPAWN_EGG.get())));
////
//    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
//    public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () ->
//            new Item(new Item.Properties().arch$tab(PumpkinPal.EXAMPLE_TAB)));

//        public static final RegistrySupplier<Item> PUMPKINPAL_SPAWN_EGG = ITEMS.register("pumpkinpal_spawn_egg", () ->
//                new ArchitecturySpawnEggItem((RegistrySupplier<? extends EntityType<? extends Mob>>) PumpkinPalPlatform.getPumpkinPal(), 0xffffff, 0xffffff, new Item.Properties()));
    
    public static void init() {
//        TABS.register();
        ENTITY_TYPES.register();
        MOB_EFFECTS.register();
        ITEMS.register();

        EntityAttributeRegistry.register(PUMPKINPAL, PumpkinPalEntity::pumpkinPalAttributes);
        
//        System.out.println(PumpkinPalPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());

        EnvExecutor.runInEnv(Env.CLIENT, () -> InitializeClientRenderer::initializeClient);
    }

    @Environment(EnvType.CLIENT)
    public static class InitializeClientRenderer {
        @Environment(EnvType.CLIENT)
        public static void initializeClient() {
            EntityRendererRegistry.register(PUMPKINPAL, PumpkinPalRenderer::new);
        }

    }
}
