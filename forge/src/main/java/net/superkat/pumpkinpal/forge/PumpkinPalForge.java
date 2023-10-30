package net.superkat.pumpkinpal.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.superkat.pumpkinpal.PumpkinPal;

@Mod(PumpkinPal.MOD_ID)
public class PumpkinPalForge {

    public PumpkinPalForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(PumpkinPal.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

//        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        PumpkinPal.init();
    }
}
