package net.superkat.pumpkinpal.fabric;

import net.fabricmc.api.ModInitializer;
import net.superkat.pumpkinpal.PumpkinPal;
import software.bernie.geckolib.GeckoLib;

public class PumpkinPalFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        PumpkinPal.init();

        GeckoLib.initialize();
    }
}
