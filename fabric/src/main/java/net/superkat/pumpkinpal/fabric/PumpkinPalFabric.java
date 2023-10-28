package net.superkat.pumpkinpal.fabric;

import net.fabricmc.api.ModInitializer;
import net.superkat.pumpkinpal.PumpkinPal;

public class PumpkinPalFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PumpkinPal.init();
    }
}
