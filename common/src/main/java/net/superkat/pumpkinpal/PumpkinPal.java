package net.superkat.pumpkinpal;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;

import java.util.function.Supplier;

public class PumpkinPal {
    public static final String MOD_ID = "pumpkinpal";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

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
//        ITEMS.register();
        
        System.out.println(PumpkinPalPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
