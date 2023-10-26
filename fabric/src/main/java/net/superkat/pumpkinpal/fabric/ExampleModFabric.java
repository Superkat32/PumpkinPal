package net.superkat.pumpkinpal.fabric;

import net.fabricmc.api.ModInitializer;
import net.superkat.pumpkinpal.ExampleMod;

public class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleMod.init();
    }
}
